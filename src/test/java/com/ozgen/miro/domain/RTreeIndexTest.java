package com.ozgen.miro.domain;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RTreeIndexTest {
    private Database database;
    private Map<String, WidgetEntity> inMemoryDatabaseEngine;
    private RTreeIndexer rTreeIndexer;

    @SuppressWarnings("unchecked")
    @Before
    public void init() throws Exception {
        inMemoryDatabaseEngine = mock(Map.class);
        rTreeIndexer = new RTreeIndexer();
        database = new InMemoryDatabase(inMemoryDatabaseEngine, rTreeIndexer);
    }

    @Test
    public void getAllWidgets_spatialQuery() throws PageNotFoundException, WidgetAlreadyExistsException {
        database.insert(new WidgetEntity.Builder().withId("0").withHeight(10).withWidth(10).withXCoordinate(0)
                .withYCoordinate(0).withzIndex(1).build());
        database.insert(new WidgetEntity.Builder().withId("1").withHeight(100).withWidth(100).withXCoordinate(0)
                .withYCoordinate(0).withzIndex(2).build());
        PageData<WidgetEntity> pageData = database.getAll(0, 10, 45, 45, 100, 100);
        assertEquals(0, pageData.getWidgetEntities().length);
        pageData = database.getAll(0, 10, 4, 4, 100, 100);
        assertEquals(0, pageData.getWidgetEntities().length);
        pageData = database.getAll(0, 10, -5, -5, 5, 5);
        assertEquals(1, pageData.getWidgetEntities().length);
        pageData = database.getAll(0, 10, -50, -50, 50, 50);
        assertEquals(2, pageData.getWidgetEntities().length);
    }
}
