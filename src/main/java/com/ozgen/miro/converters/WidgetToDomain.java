package com.ozgen.miro.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.ozgen.miro.controllers.dto.WidgetDTO;
import com.ozgen.miro.domain.WidgetEntity;

/**
 * This class is the converter class to convert {@link WidgetDTO} to {@link WidgetEntity}
 */
@Component
public class WidgetToDomain implements Converter<WidgetDTO, WidgetEntity> {
    /**
     * Converts {@link WidgetDTO} to {@link WidgetEntity}
     *
     * @param source {@link WidgetDTO}
     * @return {@link WidgetEntity}
     */
    @Override
    public WidgetEntity convert(WidgetDTO source) {
        if (source == null) {
            return null;
        }
        return new WidgetEntity.Builder().withId(source.getId()).withHeight(source.getHeight())
                .withWidth(source.getWidth()).withXCoordinate(source.getxCoordinate())
                .withYCoordinate(source.getyCoordinate()).withzIndex(source.getzIndex())
                .withModifiedAt(source.getModifiedAt()).build();
    }
}
