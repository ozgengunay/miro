package com.ozgen.miro.controllers.dto;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

/**
 * DTO class for widgets for client side usage
 *
 */
@JsonInclude(Include.NON_NULL)
public class WidgetDTO {
    @ApiModelProperty(value = "Unique identifier for this entity.", accessMode = AccessMode.READ_ONLY)
    @JsonProperty(access = READ_ONLY)
    private final String id;
    @ApiModelProperty(value = "X-coordinate of the widget.", dataType = "Integer")
    @JsonProperty
    private final Integer xCoordinate;
    @ApiModelProperty(value = "Y-coordinate of the widget.", dataType = "Integer")
    @JsonProperty
    private final Integer yCoordinate;
    @ApiModelProperty(value = "Z-Index of the widget.")
    @JsonProperty
    private final Integer zIndex;
    @ApiModelProperty(value = "Width of the widget.")
    @JsonProperty
    private final Integer width;
    @ApiModelProperty(value = "Height of the widget.")
    @JsonProperty
    private final Integer height;
    @ApiModelProperty(value = "Unix epoch time for last modification date.", accessMode = AccessMode.READ_ONLY)
    @JsonProperty(access = READ_ONLY)
    private final Long modifiedAt;

    /**
     * Default constructor for Jackson.
     */
    private WidgetDTO() {
        this.id = null;
        this.xCoordinate = null;
        this.yCoordinate = null;
        this.zIndex = null;
        this.width = null;
        this.height = null;
        this.modifiedAt = null;
    }

    private WidgetDTO(Builder builder) {
        this.id = builder.id;
        this.xCoordinate = builder.xCoordinatel;
        this.yCoordinate = builder.yCoordinate;
        this.zIndex = builder.zIndex;
        this.width = builder.width;
        this.height = builder.height;
        this.modifiedAt = builder.modifiedAt;
    }

    /**
     * @return Unique identifier of the widget
     */
    public String getId() {
        return id;
    }

    /**
     * @return X-coordinate of the widget
     */
    public Integer getxCoordinate() {
        return xCoordinate;
    }

    /**
     * @return Y-coordinate of the widget
     */
    public Integer getyCoordinate() {
        return yCoordinate;
    }

    /**
     * @return Z-index of the widget
     */
    public Integer getzIndex() {
        return zIndex;
    }

    /**
     * @return Width of the widget
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @return Height of the widget
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @return Last modification date of the widget
     */
    public Long getModifiedAt() {
        return modifiedAt;
    }

    /**
     * @return Converts {@link WidgetDTO} instance to {@link Builder} instance
     */
    public Builder toBuilder() {
        return new WidgetDTO.Builder(this);
    }

    /**
     * Builder class for {@link WidgetDTO}
     *
     */
    public static class Builder {
        private String id;
        private Integer xCoordinatel;
        private Integer yCoordinate;
        private Integer zIndex;
        private Integer width;
        private Integer height;
        private Long modifiedAt;

        /**
         * Constructs Builder with given {@link WidgetDTO} object
         * 
         * @param dto {@link WidgetDTO} object to copy its parameters
         */
        public Builder(WidgetDTO dto) {
            this.id = dto.id;
            this.xCoordinatel = dto.xCoordinate;
            this.yCoordinate = dto.yCoordinate;
            this.zIndex = dto.zIndex;
            this.width = dto.width;
            this.height = dto.height;
            this.modifiedAt = dto.modifiedAt;
        }

        /**
         * Default Constructor
         */
        public Builder() {
        }

        /**
         * 
         * Sets id of the widget
         * 
         * @param id Unique identifier of the widget
         * @return {@link Builder}
         */
        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        /**
         * Sets x-coordinate of the widget
         * 
         * @param xCoordinate X-coordinate of the widget
         * @return {@link Builder}
         */
        public Builder withXCoordinate(Integer xCoordinate) {
            this.xCoordinatel = xCoordinate;
            return this;
        }

        /**
         * Sets y-coordinate of the widget
         * 
         * @param yCoordinate Y-coordinate of the widget
         * @return {@link Builder}
         */
        public Builder withYCoordinate(Integer yCoordinate) {
            this.yCoordinate = yCoordinate;
            return this;
        }

        /**
         * Sets z-index of the widget
         * 
         * @param zIndex Z-index of the widget
         * @return {@link Builder}
         */
        public Builder withzIndex(Integer zIndex) {
            this.zIndex = zIndex;
            return this;
        }

        /**
         * Sets width of the widget
         * 
         * @param width Width of the widget
         * @return {@link Builder}
         */
        public Builder withWidth(Integer width) {
            this.width = width;
            return this;
        }

        /**
         * Sets height of the widget
         * 
         * @param height Height of the widget
         * @return {@link Builder}
         */
        public Builder withHeight(Integer height) {
            this.height = height;
            return this;
        }

        /**
         * Sets modifiedAt of the widget
         * 
         * @param modifiedAt Last modification date of the widget
         * @return {@link Builder}
         */
        public Builder withModifiedAt(Long modifiedAt) {
            this.modifiedAt = modifiedAt;
            return this;
        }

        /**
         * Builds new instance of <code>WidgetDTO</code> object
         * 
         * @return new instance of {@link WidgetDTO} object
         */
        public WidgetDTO build() {
            return new WidgetDTO(this);
        }
    }
}
