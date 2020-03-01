package com.ozgen.miro.domain;

/**
 * Interface for basic database operations
 *
 */
public interface Database {
    /**
     * Clears database
     * 
     */
    void clear();

    /**
     * Inserts new widget specified by {@link WidgetEntity}
     * 
     * @param widgetEntity {@link WidgetEntity} to insert
     * @throws WidgetAlreadyExistsException If Widget already exists
     */
    void insert(WidgetEntity widgetEntity) throws WidgetAlreadyExistsException;

    /**
     * Updates existing widget specified by {@link WidgetEntity}
     * 
     * @param widgetEntity {@link WidgetEntity} to update
     * @throws WidgetNotFoundException If Widget can't be found
     */
    void update(WidgetEntity widgetEntity) throws WidgetNotFoundException;

    /**
     * Deletes existing widget specified by <code>widgetId</code>
     * 
     * @param widgetId Id of the widget to delete
     * @throws WidgetNotFoundException If Widget can't be found
     */
    void delete(String widgetId) throws WidgetNotFoundException;

    /**
     * Gets a single widget specified by <code>widgetId</code>
     * 
     * @param widgetId Unique identifier of the widget
     * @return single widget
     * @throws WidgetNotFoundException If Widget can't be found
     */
    WidgetEntity get(String widgetId) throws WidgetNotFoundException;

    /**
     * Gets paged widgets
     * 
     * @param pageSize Page size parameter for paging
     * @param page     Page number parameter for paging
     * 
     * @return paged widgets
     * @throws PageNotFoundException If page doesn't exist
     */
    PageData<WidgetEntity> getAll(int page, int pageSize) throws PageNotFoundException;

    /**
     * Gets paged widgets with spatial query
     * 
     * @param pageSize Page size parameter for paging
     * @param page     Page number parameter for paging
     * @param x1       Spatial search x1 parameter
     * @param x2       Spatial search x2 parameter
     * @param y1       Spatial search y1 parameter
     * @param y2       Spatial search y2 parameter
     * @return paged widgets
     * @throws PageNotFoundException If page doesn't exist
     */
    PageData<WidgetEntity> getAll(int page, int pageSize, Integer x1, Integer y1, Integer x2, Integer y2)
            throws PageNotFoundException;
}
