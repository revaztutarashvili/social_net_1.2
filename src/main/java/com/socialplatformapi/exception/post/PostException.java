package com.socialplatformapi.exception.post;

import com.socialplatformapi.exception.BaseException;
import com.socialplatformapi.exception.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when post operations fail.
 * This includes cases like post not found, access denied, etc.
 */
public class PostException extends BaseException {
    
    public PostException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getDefaultMessage(), determineHttpStatus(errorCode));
    }
    
    public PostException(ErrorCode errorCode, String customMessage) {
        super(errorCode.getCode(), customMessage, determineHttpStatus(errorCode));
    }
    
    public PostException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getCode(), errorCode.getDefaultMessage(), determineHttpStatus(errorCode), cause);
    }
    
    public PostException(ErrorCode errorCode, String customMessage, Object... messageArgs) {
        super(errorCode.getCode(), customMessage, determineHttpStatus(errorCode), messageArgs);
    }
    
    private static HttpStatus determineHttpStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case POST_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case POST_ACCESS_DENIED -> HttpStatus.FORBIDDEN;
            case POST_INVALID_DATA -> HttpStatus.BAD_REQUEST;
            case POST_ALREADY_LIKED, POST_NOT_LIKED -> HttpStatus.CONFLICT;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}
