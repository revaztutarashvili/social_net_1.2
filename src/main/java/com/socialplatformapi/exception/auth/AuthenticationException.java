package com.socialplatformapi.exception.auth;

import com.socialplatformapi.exception.BaseException;
import com.socialplatformapi.exception.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when authentication fails.
 * This includes cases like invalid credentials, missing tokens, etc.
 */
public class AuthenticationException extends BaseException {
    
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getDefaultMessage(), HttpStatus.UNAUTHORIZED);
    }
    
    public AuthenticationException(ErrorCode errorCode, String customMessage) {
        super(errorCode.getCode(), customMessage, HttpStatus.UNAUTHORIZED);
    }
    
    public AuthenticationException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getCode(), errorCode.getDefaultMessage(), HttpStatus.UNAUTHORIZED, cause);
    }
    
    public AuthenticationException(ErrorCode errorCode, String customMessage, Object... messageArgs) {
        super(errorCode.getCode(), customMessage, HttpStatus.UNAUTHORIZED, messageArgs);
    }
}
