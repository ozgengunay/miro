package com.ozgen.miro.domain;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class InMemoryDatabasePaginationTest {
    private Database database;
    private Map<String, WidgetEntity> inMemoryDatabaseEngine;
    private RTreeIndexer rTreeIndexer;
    private int widgetsSize;
    private int page;
    private int pageSize;
    private int expectedReturnedSize;

    public InMemoryDatabasePaginationTest(int widgetsSize, int page, int pageSize, int expectedReturnedSize) {
        this.widgetsSize = widgetsSize;
        this.page = page;
        this.pageSize = pageSize;
        this.expectedReturnedSize = expectedReturnedSize;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { 24, 0, 10, 10 }, { 24, 1, 10, 10 }, { 24, 2, 10, 4 }, { 5, 0, 1, 1 },
                { 5, 5, 1, 1 }, { 5, 0, 5, 5 } });
    }

    @SuppressWarnings("unchecked")
    @Before
    public void init() throws Exception {
        inMemoryDatabaseEngine = mock(Map.class);
        rTreeIndexer = mock(RTreeIndexer.class);
        database = new InMemoryDatabase(inMemoryDatabaseEngine, rTreeIndexer);
    }

    @Test
    public void getAllWidgetsTest_paginationTest() throws WidgetNotFoundException, PageNotFoundException {
        WidgetEntity[] widgetEntities = new WidgetEntity[widgetsSize];
        for (int i = 0; i < widgetEntities.length; i++) {
            widgetEntities[i] = new WidgetEntity.Builder().withModifiedAt(System.currentTimeMillis())
                    .withId(UUID.randomUUID().toString()).withHeight(10).withWidth(20).withXCoordinate(100)
                    .withYCoordinate(150).withzIndex(1).build();
        }
        when(inMemoryDatabaseEngine.values()).thenReturn(Arrays.asList(widgetEntities));
        PageData<WidgetEntity> pageData = database.getAll(page, pageSize);
        assertTrue(pageData.getWidgetEntities().length == expectedReturnedSize);
    }
}
