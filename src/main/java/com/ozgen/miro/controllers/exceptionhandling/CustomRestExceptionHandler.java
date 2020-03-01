package com.ozgen.miro.controllers.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * This class contains handler methods for API exceptions
 *
 */
@ControllerAdvice(basePackages = "com.ozgen.miro.controllers")
public class CustomRestExceptionHandler {
    /**
     * 
     * Handler for {@link MiroRestException}
     * 
     * @param ex      {@link MiroRestException}
     * @param request {@link WebRequest}
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler({ MiroRestException.class })
    public ResponseEntity<Object> handleAll(MiroRestException ex, WebRequest request) {
        ApiError apiError = new ApiError(ex.getClaz(), ex.getDescription());
        return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * 
     * Handler for all exceptions including {@link RuntimeException}
     * 
     * @param ex      {@link Exception}
     * @param request {@link WebRequest}
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(ex.getClass().getName(), ex.getMessage() != null ? ex.getMessage() : "");
        return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
    }
}