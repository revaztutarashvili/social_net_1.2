package com.socialplatformapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base exception class for all custom exceptions in the application.
 * Provides common functionality like error codes and HTTP status codes.
 */
@Getter
public abstract class BaseException extends RuntimeException {
    
    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Object[] messageArgs;
    
    protected BaseException(String errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.messageArgs = null;
    }
    
    protected BaseException(String errorCode, String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.messageArgs = null;
    }
    
    protected BaseException(String errorCode, String message, HttpStatus httpStatus, Object... messageArgs) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.messageArgs = messageArgs;
    }
}