package com.ozgen.miro.domain;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ozgen.miro.utils.PaginationUtil;

/**
 * H2 database implementation of {@link Database}. Pagination and spatial query is implemented as in
 * {@link InMemoryDatabase}. Future release might make use of out-of-the-box RTree support of the database.
 * 
 *
 */
@Component
public class H2Database implements Database {
    private final WidgetRepository widgetRepository;
    private RTreeIndexer rTreeIndexer;

    /**
     * Constructs {@link InMemoryDatabase} with required engine parameter.
     * 
     * @param widgetRepository {@link WidgetRepository}
     * @param rTreeIndexer     RTree indexer
     */
    @Autowired
    public H2Database(WidgetRepository widgetRepository, RTreeIndexer rTreeIndexer) {
        this.widgetRepository = widgetRepository;
        this.rTreeIndexer = rTreeIndexer;
    }

    @Override
    public void clear() {
        widgetRepository.deleteAll();
    }

    @Override
    public void insert(WidgetEntity widgetEntity) throws WidgetAlreadyExistsException {
        checkArgument(widgetEntity.getId() != null, "Widget to be created should contain id");
        if (exists(widgetEntity.getId())) {
            throw new WidgetAlreadyExistsException("Widget already exists with id : " + widgetEntity.getId());
        }
        widgetRepository.save(widgetEntity);
        rTreeIndexer.addToRTreeIndex(widgetEntity);
    }

    @Override
    public void update(WidgetEntity widgetEntity) throws WidgetNotFoundException {
        checkArgument(widgetEntity.getId() != null, "Widget to be updated should contain id");
        try {
            // need to requery db for RTree library as it needs the original object to update.
            WidgetEntity entity = widgetRepository.findById(widgetEntity.getId()).get();
            rTreeIndexer.removeFromRTreeIndex(entity);
            widgetRepository.save(widgetEntity);
            rTreeIndexer.addToRTreeIndex(widgetEntity);
        } catch (NoSuchElementException e) {
            throw new WidgetNotFoundException("Widget not found with id : " + widgetEntity.getId());
        }
    }

    private boolean exists(String widgetId) {
        checkArgument(widgetId != null, "widgetId shouldn't be null");
        return widgetRepository.findById(widgetId).isPresent();
    }

    @Override
    public void delete(String widgetId) throws WidgetNotFoundException {
        checkArgument(widgetId != null, "widgetId shouldn't be null");
        try {
            WidgetEntity entity = widgetRepository.findById(widgetId).get();
            widgetRepository.deleteById(widgetId);
            rTreeIndexer.removeFromRTreeIndex(entity);
        } catch (NoSuchElementException e) {
            throw new WidgetNotFoundException("Widget not found with id : " + widgetId);
        }
    }

    @Override
    public WidgetEntity get(String widgetId) throws WidgetNotFoundException {
        try {
            checkArgument(widgetId != null, "widgetId shouldn't be null");
            return widgetRepository.findById(widgetId).get();
        } catch (NoSuchElementException e) {
            throw new WidgetNotFoundException("Widget not found with id : " + widgetId);
        }
    }

    @Override
    public PageData<WidgetEntity> getAll(int page, int pageSize) throws PageNotFoundException {
        List<WidgetEntity> widgets = new ArrayList<WidgetEntity>();
        widgetRepository.findAll().forEach(person -> widgets.add(person));
        WidgetEntity[] widgetEntities = widgets.toArray(new WidgetEntity[0]);
        return PaginationUtil.paginate(page, pageSize, widgetEntities);
    }

    @Override
    public PageData<WidgetEntity> getAll(int page, int pageSize, Integer x1, Integer y1, Integer x2, Integer y2)
            throws PageNotFoundException {
        WidgetEntity[] widgetEntities = RTreeIndexer.rTreeIndexerSearch(rTreeIndexer, x1, y1, x2, y2);
        return PaginationUtil.paginate(page, pageSize, widgetEntities);
    }
}
