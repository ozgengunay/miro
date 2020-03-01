package com.ozgen.miro.services;

import com.ozgen.miro.domain.PageData;
import com.ozgen.miro.domain.PageNotFoundException;
import com.ozgen.miro.domain.WidgetAlreadyExistsException;
import com.ozgen.miro.domain.WidgetEntity;
import com.ozgen.miro.domain.WidgetNotFoundException;

public interface WidgetService {
    /**
     * Creates new widget specified by {@link WidgetEntity}
     * 
     * @param widgetEntity {@link WidgetEntity} to create
     * @return Actual {@link WidgetEntity} object which has been created
     * @throws WidgetAlreadyExistsException If widget already exists
     */
    WidgetEntity createWidget(WidgetEntity widgetEntity) throws WidgetAlreadyExistsException;

    /**
     * Updates existing widget specified by {@link WidgetEntity}
     * 
     * @param widgetEntity {@link WidgetEntity} to update
     * @return Actual {@link WidgetEntity} object which has been updated
     * @throws WidgetNotFoundException If widget couldn't be found
     */
    WidgetEntity updateWidget(WidgetEntity widgetEntity) throws WidgetNotFoundException;

    /**
     * Gets a single widget specified by <code>widgetId</code>
     * 
     * @param widgetId Id of the widget
     * @return single widget
     * @throws WidgetNotFoundException If widget couldn't be found
     */
    WidgetEntity getWidget(String widgetId) throws WidgetNotFoundException;

    /**
     * Deletes a single widget specified by <code>widgetId</code>
     * 
     * @param widgetId Id of the widget to delete
     * @throws WidgetNotFoundException If widget couldn't be found
     */
    void deleteWidget(String widgetId) throws WidgetNotFoundException;

    /**
     * Gets paged widgets with spatial query
     * 
     * @param pageSize Page size parameter for paging
     * @param page     Page number parameter for paging
     * @param x1       Spatial search x1 parameter
     * @param y1       Spatial search y1 parameter
     * @param x2       Spatial search x2 parameter
     * @param y2       Spatial search y2 parameter
     * 
     * @return Paged widgets
     * @throws PageNotFoundException If page doesn't exist
     */
    PageData<WidgetEntity> getAllWidgets(int page, int pageSize, Integer x1, Integer y1, Integer x2, Integer y2)
            throws PageNotFoundException;

    /**
     * Gets paged widgets
     * 
     * @param pageSize Page size parameter for paging
     * @param page     Page number parameter for paging
     * 
     * @return Paged widgets
     * @throws PageNotFoundException If page doesn't exist
     */
    PageData<WidgetEntity> getAllWidgets(int page, int pageSize) throws PageNotFoundException;
}
