package com.ozgen.miro.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.ozgen.miro.domain.Database;
import com.ozgen.miro.domain.PageData;
import com.ozgen.miro.domain.PageNotFoundException;
import com.ozgen.miro.domain.WidgetAlreadyExistsException;
import com.ozgen.miro.domain.WidgetEntity;
import com.ozgen.miro.domain.WidgetNotFoundException;

public class WidgetServiceImplTest {
    private WidgetService service;
    private Database dataSource;

    @Before
    public void init() throws Exception {
        dataSource = mock(Database.class);
        service = new WidgetServiceImpl(dataSource);
    }

    @Test
    public void createWidgetTest_successful() throws WidgetAlreadyExistsException {
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withHeight(10).withWidth(20).withXCoordinate(100)
                .withYCoordinate(150).withzIndex(1).build();
        WidgetEntity createdWidgetEntity = service.createWidget(widgetEntity);
        assertNotNull(createdWidgetEntity.getModifiedAt());
        assertNotNull(createdWidgetEntity.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWidgetTest_unsuccessful_idVerification() throws WidgetAlreadyExistsException {
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withId(UUID.randomUUID().toString()).withHeight(10)
                .withWidth(20).withXCoordinate(100).withYCoordinate(150).withzIndex(1).build();
        service.createWidget(widgetEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWidgetTest_unsuccessful_modifiedAtVerification() throws WidgetAlreadyExistsException {
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withModifiedAt(12l).withHeight(10).withWidth(20)
                .withXCoordinate(100).withYCoordinate(150).withzIndex(1).build();
        service.createWidget(widgetEntity);
    }

    @Test
    public void verifyWidgetEntity_successful() {
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withHeight(10).withWidth(20).withXCoordinate(100)
                .withYCoordinate(150).build();
        ((WidgetServiceImpl) service).verifyWidgetEntity(widgetEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyWidgetEntity_unsuccessful_widgetEntityNull() {
        ((WidgetServiceImpl) service).verifyWidgetEntity(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyWidgetEntity_unsuccessful_xCoordinateNull() {
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withHeight(10).withWidth(20).withYCoordinate(150)
                .build();
        ((WidgetServiceImpl) service).verifyWidgetEntity(widgetEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyWidgetEntity_unsuccessful_yCoordinateNull() {
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withHeight(10).withWidth(20).withXCoordinate(150)
                .build();
        ((WidgetServiceImpl) service).verifyWidgetEntity(widgetEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyWidgetEntity_unsuccessful_widthNull() {
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withHeight(10).withXCoordinate(100).withYCoordinate(150)
                .build();
        ((WidgetServiceImpl) service).verifyWidgetEntity(widgetEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyWidgetEntity_unsuccessful_heightNull() throws WidgetAlreadyExistsException {
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withWidth(20).withXCoordinate(100).withYCoordinate(150)
                .build();
        ((WidgetServiceImpl) service).verifyWidgetEntity(widgetEntity);
    }

    @Test
    public void updateWidgetTest_successful() throws WidgetNotFoundException {
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withId(UUID.randomUUID().toString()).withHeight(10)
                .withWidth(20).withXCoordinate(100).withYCoordinate(150).withzIndex(1).build();
        WidgetEntity updatedWidgetEntity = service.updateWidget(widgetEntity);
        assertNotNull(updatedWidgetEntity.getModifiedAt());
        assertEquals(widgetEntity.getId(), updatedWidgetEntity.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateWidgetTest_unsuccessful_idVerification() throws WidgetNotFoundException {
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withHeight(10).withWidth(20).withXCoordinate(100)
                .withYCoordinate(150).withzIndex(1).build();
        service.updateWidget(widgetEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateWidgetTest_unsuccessful_modifiedAtVerification() throws WidgetNotFoundException {
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withId(UUID.randomUUID().toString()).withModifiedAt(12l)
                .withHeight(10).withWidth(20).withXCoordinate(100).withYCoordinate(150).withzIndex(1).build();
        service.updateWidget(widgetEntity);
    }

    @Test
    public void getWidget_successful() throws WidgetNotFoundException {
        String widgetId = UUID.randomUUID().toString();
        Long modifiedAt = System.currentTimeMillis();
        WidgetEntity widgetEntityAsParameter = new WidgetEntity.Builder().withId(widgetId).withModifiedAt(modifiedAt)
                .withHeight(10).withWidth(20).withXCoordinate(100).withYCoordinate(150).withzIndex(1).build();
        when(dataSource.get(widgetId)).thenReturn(widgetEntityAsParameter);
        WidgetEntity returnedWidget = service.getWidget(widgetId);
        assertEquals(widgetId, returnedWidget.getId());
        assertEquals(modifiedAt, returnedWidget.getModifiedAt());
        assertEquals(widgetEntityAsParameter.getHeight(), returnedWidget.getHeight());
        assertEquals(widgetEntityAsParameter.getWidth(), returnedWidget.getWidth());
        assertEquals(widgetEntityAsParameter.getzIndex(), returnedWidget.getzIndex());
        assertEquals(widgetEntityAsParameter.getxCoordinate(), returnedWidget.getxCoordinate());
        assertEquals(widgetEntityAsParameter.getyCoordinate(), returnedWidget.getyCoordinate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getWidget_unsuccessful_idVerification() throws WidgetNotFoundException {
        service.getWidget(null);
    }

    @Test
    public void deleteWidget_successful() throws WidgetNotFoundException {
        String widgetId = UUID.randomUUID().toString();
        service.deleteWidget(widgetId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deletetWidget_unsuccessful_idVerification() throws WidgetNotFoundException {
        service.deleteWidget(null);
    }

    @Test
    public void getAllWidgets() throws PageNotFoundException {
        String widgetId1 = UUID.randomUUID().toString();
        String widgetId2 = UUID.randomUUID().toString();
        Long modifiedAt1 = System.currentTimeMillis();
        try {
            // the sleep is to have modifiedAt2 different than modifiedAt1
            Thread.sleep(2);
        } catch (InterruptedException e) {
            // ignore
        }
        Long modifiedAt2 = System.currentTimeMillis();
        WidgetEntity[] widgetEntities = new WidgetEntity[2];
        widgetEntities[0] = new WidgetEntity.Builder().withModifiedAt(modifiedAt1).withId(widgetId1).withHeight(10)
                .withWidth(20).withXCoordinate(100).withYCoordinate(150).withzIndex(1).build();
        widgetEntities[1] = new WidgetEntity.Builder().withModifiedAt(modifiedAt2).withId(widgetId2).withHeight(11)
                .withWidth(21).withXCoordinate(101).withYCoordinate(151).withzIndex(2).build();
        when(dataSource.getAll(0, 10))
                .thenReturn(new PageData<WidgetEntity>(widgetEntities, 0, 1, 2));
        PageData<WidgetEntity> pagedData = service.getAllWidgets(0, 10);
        assertEquals(widgetId1, pagedData.getWidgetEntities()[0].getId());
        assertEquals(widgetId2, pagedData.getWidgetEntities()[1].getId());
        assertEquals(modifiedAt1, pagedData.getWidgetEntities()[0].getModifiedAt());
        assertEquals(modifiedAt2, pagedData.getWidgetEntities()[1].getModifiedAt());
    }
}
