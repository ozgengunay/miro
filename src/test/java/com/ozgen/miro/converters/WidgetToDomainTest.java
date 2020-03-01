package com.ozgen.miro.converters;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

import com.ozgen.miro.controllers.dto.WidgetDTO;
import com.ozgen.miro.domain.WidgetAlreadyExistsException;
import com.ozgen.miro.domain.WidgetEntity;

public class WidgetToDomainTest {
    @Test
    public void test() throws WidgetAlreadyExistsException {
        WidgetToDomain converter = new WidgetToDomain();
        WidgetDTO widgetDTO = new WidgetDTO.Builder().withModifiedAt(System.currentTimeMillis())
                .withId(UUID.randomUUID().toString()).withHeight(1).withWidth(20).withXCoordinate(100)
                .withYCoordinate(150).withzIndex(1).build();
        WidgetEntity widgetEntity = converter.convert(widgetDTO);
        assertEquals(widgetDTO.getHeight(), widgetEntity.getHeight());
        assertEquals(widgetDTO.getId(), widgetEntity.getId());
        assertEquals(widgetDTO.getModifiedAt(), widgetEntity.getModifiedAt());
        assertEquals(widgetDTO.getWidth(), widgetEntity.getWidth());
        assertEquals(widgetDTO.getxCoordinate(), widgetEntity.getxCoordinate());
        assertEquals(widgetDTO.getyCoordinate(), widgetEntity.getyCoordinate());
        assertEquals(widgetDTO.getzIndex(), widgetEntity.getzIndex());
    }
}
