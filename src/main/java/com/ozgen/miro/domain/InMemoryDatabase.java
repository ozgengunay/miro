package com.ozgen.miro.domain;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.ozgen.miro.utils.PaginationUtil;

/**
 * In-memory implementation of {@link Database}
 *
 */
@Component
@Primary
public class InMemoryDatabase implements Database {
    private Map<String, WidgetEntity> inMemoryDatabaseEngine;
    private RTreeIndexer rTreeIndexer;

    /**
     * Constructs {@link InMemoryDatabase} with required engine parameter.
     * 
     * @param inMemoryDatabaseEngine database engine
     * @param rTreeIndexer           RTree indexer
     */
    @Autowired
    public InMemoryDatabase(Map<String, WidgetEntity> inMemoryDatabaseEngine, RTreeIndexer rTreeIndexer) {
        this.inMemoryDatabaseEngine = inMemoryDatabaseEngine;
        this.rTreeIndexer = rTreeIndexer;
    }

    @Override
    public void insert(WidgetEntity widgetEntity) throws WidgetAlreadyExistsException {
        checkArgument(widgetEntity.getId() != null, "Widget to be created should contain id");
        if (exists(widgetEntity.getId())) {
            throw new WidgetAlreadyExistsException("Widget already exists with id : " + widgetEntity.getId());
        }
        inMemoryDatabaseEngine.put(widgetEntity.getId(), widgetEntity);
        rTreeIndexer.addToRTreeIndex(widgetEntity);
    }

    private boolean exists(String widgetId) {
        return inMemoryDatabaseEngine.containsKey(widgetId);
    }

    @Override
    public void update(WidgetEntity widgetEntity) throws WidgetNotFoundException {
        checkArgument(widgetEntity.getId() != null, "Widget to be updated should contain id");
        if (!exists(widgetEntity.getId())) {
            throw new WidgetNotFoundException("Widget not found with id : " + widgetEntity.getId());
        }
        inMemoryDatabaseEngine.put(widgetEntity.getId(), widgetEntity);
        rTreeIndexer.removeFromRTreeIndex(widgetEntity);
        rTreeIndexer.addToRTreeIndex(widgetEntity);
    }

    @Override
    public void delete(String widgetId) throws WidgetNotFoundException {
        checkArgument(widgetId != null, "widgetId shouldn't be null");
        WidgetEntity widgetEntity = inMemoryDatabaseEngine.remove(widgetId);
        if (widgetEntity == null) {
            throw new WidgetNotFoundException("Widget not found with id : " + widgetId);
        }
        rTreeIndexer.removeFromRTreeIndex(widgetEntity);
    }

    @Override
    public WidgetEntity get(String widgetId) throws WidgetNotFoundException {
        checkArgument(widgetId != null, "widgetId shouldn't be null");
        WidgetEntity widgetEntity = inMemoryDatabaseEngine.get(widgetId);
        if (widgetEntity == null) {
            throw new WidgetNotFoundException("Widget not found with id : " + widgetId);
        }
        return widgetEntity;
    }

    @Override
    public PageData<WidgetEntity> getAll(int page, int pageSize) throws PageNotFoundException {
        // toArray helps us to break link between map and collection returned by "values" call. So the changes in
        // map will not reflect to returned array.
        WidgetEntity[] widgetEntities = inMemoryDatabaseEngine.values().toArray(new WidgetEntity[0]);
        return PaginationUtil.paginate(page, pageSize, widgetEntities);
    }

    @Override
    public PageData<WidgetEntity> getAll(int page, int pageSize, Integer x1, Integer y1, Integer x2, Integer y2)
            throws PageNotFoundException {
        WidgetEntity[] widgetEntities = RTreeIndexer.rTreeIndexerSearch(rTreeIndexer, x1, y1, x2, y2);
        return PaginationUtil.paginate(page, pageSize, widgetEntities);
    }

    @Override
    public void clear() {
        inMemoryDatabaseEngine.clear();
    }
}
