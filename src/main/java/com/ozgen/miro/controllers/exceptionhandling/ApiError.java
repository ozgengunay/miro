package com.ozgen.miro.controllers.exceptionhandling;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Class which encapsulate error details
 *
 */
@JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
@JsonTypeName(value = "error")
public class ApiError {
    private String claz;
    private String description;

    /**
     * Constructs {@link ApiError} object with given parameters
     * 
     * @param claz        Fully qualified class name
     * @param description Description of the Error
     */
    public ApiError(String claz, String description) {
        this.claz = claz;
        this.description = description;
    }

    /**
     * Returns fully qualified class name of the error
     * 
     * @return Fully qualified class name
     */
    public String getClaz() {
        return claz;
    }

    /**
     * Returns description of the error
     * 
     * @return Description of the error
     */
    public String getDescription() {
        return description;
    }
}