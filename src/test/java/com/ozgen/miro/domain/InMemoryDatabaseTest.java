package com.ozgen.miro.domain;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class InMemoryDatabaseTest {
    private Database database;
    private Map<String, WidgetEntity> inMemoryDatabaseEngine;
    private RTreeIndexer rTreeIndexer;

    @SuppressWarnings("unchecked")
    @Before
    public void init() throws Exception {
        inMemoryDatabaseEngine = mock(Map.class);
        rTreeIndexer = mock(RTreeIndexer.class);
        database = new InMemoryDatabase(inMemoryDatabaseEngine, rTreeIndexer);
    }

    @Test
    public void insertWidgetTest_successful() throws WidgetAlreadyExistsException {
        String widgetId = UUID.randomUUID().toString();
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withId(widgetId)
                .withModifiedAt(System.currentTimeMillis()).withHeight(10).withWidth(20).withXCoordinate(100)
                .withYCoordinate(150).withzIndex(1).build();
        when(inMemoryDatabaseEngine.containsKey(widgetId)).thenReturn(false);
        database.insert(widgetEntity);
    }

    @Test(expected = WidgetAlreadyExistsException.class)
    public void insertWidgetTest_unsuccessful_widgetAlreadyExists() throws WidgetAlreadyExistsException {
        String widgetId = UUID.randomUUID().toString();
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withId(widgetId)
                .withModifiedAt(System.currentTimeMillis()).withHeight(10).withWidth(20).withXCoordinate(100)
                .withYCoordinate(150).withzIndex(1).build();
        when(inMemoryDatabaseEngine.containsKey(widgetId)).thenReturn(true);
        database.insert(widgetEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertWidgetTest_unsuccessful_nullId() throws WidgetAlreadyExistsException {
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withModifiedAt(System.currentTimeMillis()).withHeight(10)
                .withWidth(20).withXCoordinate(100).withYCoordinate(150).withzIndex(1).build();
        database.insert(widgetEntity);
    }

    @Test
    public void updateWidgetTest_successful() throws WidgetNotFoundException {
        String widgetId = UUID.randomUUID().toString();
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withId(widgetId)
                .withModifiedAt(System.currentTimeMillis()).withHeight(10).withWidth(20).withXCoordinate(100)
                .withYCoordinate(150).withzIndex(1).build();
        when(inMemoryDatabaseEngine.containsKey(widgetId)).thenReturn(true);
        database.update(widgetEntity);
    }

    @Test(expected = WidgetNotFoundException.class)
    public void updateWidgetTest_unsuccessful_widgetNotFound() throws WidgetNotFoundException {
        String widgetId = UUID.randomUUID().toString();
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withId(widgetId)
                .withModifiedAt(System.currentTimeMillis()).withHeight(10).withWidth(20).withXCoordinate(100)
                .withYCoordinate(150).withzIndex(1).build();
        when(inMemoryDatabaseEngine.containsKey(widgetId)).thenReturn(false);
        database.update(widgetEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateWidgetTest_unsuccessful_nullId() throws WidgetNotFoundException {
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withModifiedAt(System.currentTimeMillis()).withHeight(10)
                .withWidth(20).withXCoordinate(100).withYCoordinate(150).withzIndex(1).build();
        database.update(widgetEntity);
    }

    @Test
    public void deleteWidgetTest_successful() throws WidgetNotFoundException {
        String widgetId = UUID.randomUUID().toString();
        Long modifiedAt = System.currentTimeMillis();
        when(inMemoryDatabaseEngine.remove(widgetId))
                .thenReturn(new WidgetEntity.Builder().withId(widgetId).withModifiedAt(modifiedAt).withHeight(10)
                        .withWidth(20).withXCoordinate(100).withYCoordinate(150).withzIndex(1).build());
        database.delete(widgetId);
    }

    @Test(expected = WidgetNotFoundException.class)
    public void deleteWidgetTest_unsuccessful_widgetNotFound() throws WidgetNotFoundException {
        String widgetId = UUID.randomUUID().toString();
        when(inMemoryDatabaseEngine.remove(widgetId)).thenReturn(null);
        database.delete(widgetId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteWidgetTest_unsuccessful_nullId() throws WidgetNotFoundException {
        database.delete(null);
    }

    @Test
    public void getWidgetTest_successful() throws WidgetNotFoundException {
        String widgetId = UUID.randomUUID().toString();
        when(inMemoryDatabaseEngine.get(widgetId))
                .thenReturn(new WidgetEntity.Builder().withId(widgetId).withModifiedAt(System.currentTimeMillis())
                        .withHeight(10).withWidth(20).withXCoordinate(100).withYCoordinate(150).withzIndex(1).build());
        database.get(widgetId);
    }

    @Test(expected = WidgetNotFoundException.class)
    public void getWidgetTest_unsuccessful_widgetNotFound() throws WidgetNotFoundException {
        String widgetId = UUID.randomUUID().toString();
        when(inMemoryDatabaseEngine.remove(widgetId)).thenReturn(null);
        database.get(widgetId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getWidgetTest_unsuccessful_nullId() throws WidgetNotFoundException {
        database.get(null);
    }

    @Test
    public void getAllWidgetsTest() throws WidgetNotFoundException, PageNotFoundException {
        String widgetId1 = UUID.randomUUID().toString();
        String widgetId2 = UUID.randomUUID().toString();
        WidgetEntity[] widgetEntities = new WidgetEntity[2];
        widgetEntities[0] = new WidgetEntity.Builder().withId(widgetId1).withModifiedAt(System.currentTimeMillis())
                .withHeight(10).withWidth(20).withXCoordinate(100).withYCoordinate(150).withzIndex(1).build();
        widgetEntities[1] = new WidgetEntity.Builder().withId(widgetId2).withModifiedAt(System.currentTimeMillis())
                .withHeight(11).withWidth(21).withXCoordinate(101).withYCoordinate(151).withzIndex(2).build();
        when(inMemoryDatabaseEngine.values()).thenReturn(Arrays.asList(widgetEntities));
        PageData<WidgetEntity> pageData = database.getAll(0, 10);
        assertEquals(widgetEntities[0], pageData.getWidgetEntities()[0]);
        assertEquals(widgetEntities[1], pageData.getWidgetEntities()[1]);
    }

    @Test
    public void getAllWidgets_testZIndexSorting() throws PageNotFoundException {
        String widgetId1 = UUID.randomUUID().toString();
        String widgetId2 = UUID.randomUUID().toString();
        Long modifiedAt1 = System.currentTimeMillis();
        try {
            // this is to have modifiedAt2 different than modifiedAt1
            Thread.sleep(2);
        } catch (InterruptedException e) {
            // ignore
        }
        Long modifiedAt2 = System.currentTimeMillis();
        WidgetEntity[] widgetEntities = new WidgetEntity[2];
        widgetEntities[0] = new WidgetEntity.Builder().withModifiedAt(modifiedAt1).withId(widgetId1).withHeight(10)
                .withWidth(20).withXCoordinate(100).withYCoordinate(150).withzIndex(1).build();
        widgetEntities[1] = new WidgetEntity.Builder().withModifiedAt(modifiedAt2).withId(widgetId2).withHeight(11)
                .withWidth(21).withXCoordinate(101).withYCoordinate(151).withzIndex(-1).build();
        when(inMemoryDatabaseEngine.values()).thenReturn(Arrays.asList(widgetEntities));
        PageData<WidgetEntity> pageData = database.getAll(0, 10);
        assertEquals(widgetId2, pageData.getWidgetEntities()[0].getId());
        assertEquals(widgetId1, pageData.getWidgetEntities()[1].getId());
        assertEquals(modifiedAt2, pageData.getWidgetEntities()[0].getModifiedAt());
        assertEquals(modifiedAt1, pageData.getWidgetEntities()[1].getModifiedAt());
    }
}
