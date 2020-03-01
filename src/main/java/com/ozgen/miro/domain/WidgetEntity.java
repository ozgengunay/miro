package com.ozgen.miro.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity class for widgets for server side usage
 *
 */
@Entity(name = "widgets")
public class WidgetEntity implements Comparable<WidgetEntity> {
    @Id
    private final String id;
    private final Integer xCoordinate;
    private final Integer yCoordinate;
    private final Integer zIndex;
    private final Integer width;
    private final Integer height;
    private final Long modifiedAt;

    // for persistence
    public WidgetEntity() {
        this.id = null;
        this.xCoordinate = null;
        this.yCoordinate = null;
        this.zIndex = null;
        this.width = null;
        this.height = null;
        this.modifiedAt = null;
    }

    private WidgetEntity(Builder builder) {
        this.id = builder.id;
        this.xCoordinate = builder.xCoordinate;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WidgetEntity other = (WidgetEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int compareTo(WidgetEntity o) {
        if (o == null)
            return 1;
        if (this.zIndex == null) {
            if (o.zIndex == null) {
                return this.id.compareTo(o.id);
            } else {
                return 1;
            }
        } else {
            if (o.zIndex == null) {
                return -1;
            } else {
                int comparisonResult = this.zIndex.compareTo(o.zIndex);
                if (comparisonResult == 0)
                    return this.id.compareTo(o.id);
                else
                    return comparisonResult;
            }
        }
    }

    /**
     * @return Converts this instance to a builder instance
     */
    public Builder toBuilder() {
        return new WidgetEntity.Builder(this);
    }

    /**
     * Builder class for ${@link WidgetEntity}
     *
     */
    public static class Builder {
        private String id;
        private Integer xCoordinate;
        private Integer yCoordinate;
        private Integer zIndex;
        private Integer width;
        private Integer height;
        private Long modifiedAt;

        /**
         * Constructs Builder with given ${@link WidgetEntity}
         * 
         * @param entity ${@link WidgetEntity} to copy its parameters
         */
        public Builder(WidgetEntity entity) {
            this.id = entity.id;
            this.xCoordinate = entity.xCoordinate;
            this.yCoordinate = entity.yCoordinate;
            this.zIndex = entity.zIndex;
            this.width = entity.width;
            this.height = entity.height;
            this.modifiedAt = entity.modifiedAt;
        }

        /**
         * Default constructor
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
            this.xCoordinate = xCoordinate;
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
         * @param modifiedAt last modification date of the widget
         * @return {@link Builder}
         */
        public Builder withModifiedAt(Long modifiedAt) {
            this.modifiedAt = modifiedAt;
            return this;
        }

        /**
         * Builds new instance of {@link WidgetEntity}
         * 
         * @return new instance of {@link WidgetEntity}
         */
        public WidgetEntity build() {
            return new WidgetEntity(this);
        }
    }
}
