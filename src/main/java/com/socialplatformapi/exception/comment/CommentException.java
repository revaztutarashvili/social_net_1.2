package com.socialplatformapi.exception.comment;

import com.socialplatformapi.exception.BaseException;
import com.socialplatformapi.exception.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when comment operations fail.
 * This includes cases like comment not found, access denied, etc.
 */
public class CommentException extends BaseException {
    
    public CommentException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getDefaultMessage(), determineHttpStatus(errorCode));
    }
    
    public CommentException(ErrorCode errorCode, String customMessage) {
        super(errorCode.getCode(), customMessage, determineHttpStatus(errorCode));
    }
    
    public CommentException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getCode(), errorCode.getDefaultMessage(), determineHttpStatus(errorCode), cause);
    }
    
    public CommentException(ErrorCode errorCode, String customMessage, Object... messageArgs) {
        super(errorCode.getCode(), customMessage, determineHttpStatus(errorCode), messageArgs);
    }
    
    private static HttpStatus determineHttpStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case COMMENT_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case COMMENT_ACCESS_DENIED -> HttpStatus.FORBIDDEN;
            case COMMENT_INVALID_DATA -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}
