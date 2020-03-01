package com.ozgen.miro.converters;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

import com.ozgen.miro.controllers.dto.WidgetDTO;
import com.ozgen.miro.domain.WidgetAlreadyExistsException;
import com.ozgen.miro.domain.WidgetEntity;

public class WidgetFromDomainTest {
    @Test
    public void test() throws WidgetAlreadyExistsException {
        WidgetFromDomain converter = new WidgetFromDomain();
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withModifiedAt(System.currentTimeMillis())
                .withId(UUID.randomUUID().toString()).withHeight(10).withWidth(20).withXCoordinate(100)
                .withYCoordinate(150).withzIndex(1).build();
        WidgetDTO widgetDTO = converter.convert(widgetEntity);
        assertEquals(widgetEntity.getHeight(), widgetDTO.getHeight());
        assertEquals(widgetEntity.getId(), widgetDTO.getId());
        assertEquals(widgetEntity.getModifiedAt(), widgetDTO.getModifiedAt());
        assertEquals(widgetEntity.getWidth(), widgetDTO.getWidth());
        assertEquals(widgetEntity.getxCoordinate(), widgetDTO.getxCoordinate());
        assertEquals(widgetEntity.getyCoordinate(), widgetDTO.getyCoordinate());
        assertEquals(widgetEntity.getzIndex(), widgetDTO.getzIndex());
    }
}
