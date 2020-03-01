package com.ozgen.miro.converters;

import java.util.Arrays;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.ozgen.miro.controllers.dto.WidgetDTO;
import com.ozgen.miro.domain.WidgetEntity;

/**
 * This class is the converter class to convert {@link WidgetEntity} to {@link WidgetDTO}
 */
@Component
public class WidgetFromDomain implements Converter<WidgetEntity, WidgetDTO> {
    /**
     * Converts {@link WidgetEntity} to {@link WidgetDTO}
     *
     * @param source {@link WidgetEntity} to convert
     * @return {@link WidgetDTO}
     */
    @Override
    public WidgetDTO convert(final WidgetEntity source) {
        if (source == null) {
            return null;
        }
        return new WidgetDTO.Builder().withId(source.getId()).withHeight(source.getHeight())
                .withWidth(source.getWidth()).withXCoordinate(source.getxCoordinate())
                .withYCoordinate(source.getyCoordinate()).withzIndex(source.getzIndex())
                .withModifiedAt(source.getModifiedAt()).build();
    }

    /**
     * Converts array of {@link WidgetEntity} objects to array of {@link WidgetDTO} objects
     * 
     * @param widgetEntities    Array of {@link WidgetEntity} objects
     * @param conversionService {@link ConversionService}
     * @return Array of {@link WidgetDTO} objects
     */
    public static WidgetDTO[] convertWidgets(WidgetEntity[] widgetEntities, ConversionService conversionService) {
        if (widgetEntities == null) {
            return null;
        }
        return Arrays.stream(widgetEntities)
                .map(widgetEntity -> conversionService.convert(widgetEntity, WidgetDTO.class))
                .toArray(WidgetDTO[]::new);
    }
}
