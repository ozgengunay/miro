package com.ozgen.miro.controllers.exceptionhandling;

/**
 * Top level exception for widget endpoints which will be handled by {@link CustomRestExceptionHandler}
 * 
 */
public class MiroRestException extends Exception {
    private String description;
    private String claz;

    /**
     * @param e {@link Exception}
     */
    public MiroRestException(Exception e) {
        super(e);
        this.claz = e.getClass().getName();
        this.description = e.getMessage() != null ? e.getMessage() : "";
    }

    /**
     * @param errorCode {@link ErrorCode}
     */
    public MiroRestException(ErrorCode errorCode) {
        super(errorCode.toString());
        this.claz = Exception.class.getName();
        this.description = errorCode.toString();
    }

    /**
     * Returns description of the error
     * 
     * @return Description of the error
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns fully qualified class name of the error
     * 
     * @return Fully qualified class name
     */
    public String getClaz() {
        return claz;
    }
}
