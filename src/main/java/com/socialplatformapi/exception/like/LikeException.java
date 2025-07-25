package com.socialplatformapi.exception.like;

import com.socialplatformapi.exception.BaseException;
import com.socialplatformapi.exception.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when like operations fail.
 * This includes cases like duplicate likes, like not found, etc.
 */
public class LikeException extends BaseException {
    
    public LikeException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getDefaultMessage(), determineHttpStatus(errorCode));
    }
    
    public LikeException(ErrorCode errorCode, String customMessage) {
        super(errorCode.getCode(), customMessage, determineHttpStatus(errorCode));
    }
    
    public LikeException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getCode(), errorCode.getDefaultMessage(), determineHttpStatus(errorCode), cause);
    }
    
    public LikeException(ErrorCode errorCode, String customMessage, Object... messageArgs) {
        super(errorCode.getCode(), customMessage, determineHttpStatus(errorCode), messageArgs);
    }
    
    private static HttpStatus determineHttpStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case LIKE_ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case LIKE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}
