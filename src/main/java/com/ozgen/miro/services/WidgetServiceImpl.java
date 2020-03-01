package com.ozgen.miro.services;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ozgen.miro.domain.Database;
import com.ozgen.miro.domain.PageData;
import com.ozgen.miro.domain.PageNotFoundException;
import com.ozgen.miro.domain.WidgetAlreadyExistsException;
import com.ozgen.miro.domain.WidgetEntity;
import com.ozgen.miro.domain.WidgetNotFoundException;

@Service
public class WidgetServiceImpl implements WidgetService {
    private Database database;

    @Autowired
    public WidgetServiceImpl(Database dataStructure) {
        this.database = dataStructure;
    }

    @Override
    public WidgetEntity createWidget(WidgetEntity widgetEntity) throws WidgetAlreadyExistsException {
        verifyWidgetEntity(widgetEntity);
        checkArgument(widgetEntity.getId() == null, "Widget to be created should not have id preset");
        checkArgument(widgetEntity.getModifiedAt() == null, "Widget to be created should not have modifiedAt preset");
        WidgetEntity updatedWidgetEntity = widgetEntity.toBuilder().withId(UUID.randomUUID().toString())
                .withModifiedAt(System.currentTimeMillis()).build();
        database.insert(updatedWidgetEntity);
        return updatedWidgetEntity;
    }

    void verifyWidgetEntity(WidgetEntity widgetEntity) {
        checkArgument(widgetEntity != null, "widgetEntity should not be null");
        checkArgument(widgetEntity.getWidth() != null && widgetEntity.getWidth() > 0,
                "widgetEntity should have width set and should be positive");
        checkArgument(widgetEntity.getHeight() != null && widgetEntity.getHeight() > 0,
                "widgetEntity should have height set and should be positive");
        checkArgument(widgetEntity.getxCoordinate() != null, "widgetEntity should have x coordinate set");
        checkArgument(widgetEntity.getyCoordinate() != null, "widgetEntity should have y coordinate set");
    }

    @Override
    public WidgetEntity updateWidget(WidgetEntity widgetEntity) throws WidgetNotFoundException {
        verifyWidgetEntity(widgetEntity);
        checkArgument(widgetEntity.getId() != null, "Widget to be updated should contain id");
        checkArgument(widgetEntity.getModifiedAt() == null, "Widget to be updated should not have modifiedAt preset");
        WidgetEntity updatedWidgetEntity = widgetEntity.toBuilder().withModifiedAt(System.currentTimeMillis()).build();
        database.update(updatedWidgetEntity);
        return updatedWidgetEntity;
    }

    @Override
    public WidgetEntity getWidget(String widgetId) throws WidgetNotFoundException {
        checkArgument(widgetId != null, "widgetId shouldn't be null");
        return database.get(widgetId);
    }

    @Override
    public PageData<WidgetEntity> getAllWidgets(int page, int pageSize) throws PageNotFoundException {
        PageData<WidgetEntity> pageData = database.getAll(page, pageSize);
        return pageData;
    }

    @Override
    public PageData<WidgetEntity> getAllWidgets(int page, int pageSize, Integer x1, Integer y1, Integer x2, Integer y2)
            throws PageNotFoundException {
        PageData<WidgetEntity> pageData = database.getAll(page, pageSize, x1, y1, x2, y2);
        return pageData;
    }

    @Override
    public void deleteWidget(String widgetId) throws WidgetNotFoundException {
        checkArgument(widgetId != null, "widgetId shouldn't be null");
        database.delete(widgetId);
    }
}
