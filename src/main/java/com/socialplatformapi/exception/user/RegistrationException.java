package com.socialplatformapi.exception.user;

import com.socialplatformapi.exception.BaseException;
import com.socialplatformapi.exception.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when user registration fails.
 * This includes cases like duplicate username/email, invalid data, etc.
 */
public class RegistrationException extends BaseException {
    
    public RegistrationException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getDefaultMessage(), HttpStatus.CONFLICT);
    }
    
    public RegistrationException(ErrorCode errorCode, String customMessage) {
        super(errorCode.getCode(), customMessage, HttpStatus.CONFLICT);
    }
    
    public RegistrationException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getCode(), errorCode.getDefaultMessage(), HttpStatus.CONFLICT, cause);
    }
    
    public RegistrationException(ErrorCode errorCode, String customMessage, Object... messageArgs) {
        super(errorCode.getCode(), customMessage, HttpStatus.CONFLICT, messageArgs);
    }
}
