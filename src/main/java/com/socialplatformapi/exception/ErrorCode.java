package com.socialplatformapi.exception;

/**
 * Standardized error codes for the application.
 * Each error code should be unique and descriptive.
 */
public enum ErrorCode {
    
    // Authentication & Authorization Errors (AUTH_xxx)
    AUTH_MISSING_TOKEN("AUTH_001", "Authentication token is missing"),
    AUTH_INVALID_TOKEN("AUTH_002", "Authentication token is invalid or expired"),
    AUTH_INVALID_CREDENTIALS("AUTH_003", "Invalid email or password"),
    AUTH_ACCESS_DENIED("AUTH_004", "Access denied to this resource"),
    
    // User Registration Errors (USER_xxx) 
    USER_USERNAME_EXISTS("USER_001", "Username already exists"),
    USER_EMAIL_EXISTS("USER_002", "Email already exists"),
    USER_NOT_FOUND("USER_003", "User not found"),
    USER_INVALID_DATA("USER_004", "Invalid user data provided"),
    
    // Post Errors (POST_xxx)
    POST_NOT_FOUND("POST_001", "Post not found"),
    POST_ACCESS_DENIED("POST_002", "You are not authorized to modify this post"),
    POST_INVALID_DATA("POST_003", "Invalid post data provided"),
    POST_ALREADY_LIKED("POST_004", "Post is already liked by this user"),
    POST_NOT_LIKED("POST_005", "Post is not liked by this user"),
    
    // Comment Errors (COMMENT_xxx)
    COMMENT_NOT_FOUND("COMMENT_001", "Comment not found"),
    COMMENT_ACCESS_DENIED("COMMENT_002", "You are not authorized to modify this comment"),
    COMMENT_INVALID_DATA("COMMENT_003", "Invalid comment data provided"),
    
    // Like Errors (LIKE_xxx)
    LIKE_ALREADY_EXISTS("LIKE_001", "Like already exists"),
    LIKE_NOT_FOUND("LIKE_002", "Like not found"),
    
    // Validation Errors (VALIDATION_xxx)
    VALIDATION_FAILED("VALIDATION_001", "Input validation failed"),
    VALIDATION_CONSTRAINT_VIOLATION("VALIDATION_002", "Constraint validation failed"),
    
    // System Errors (SYSTEM_xxx)
    SYSTEM_INTERNAL_ERROR("SYSTEM_001", "Internal server error occurred"),
    SYSTEM_SERVICE_UNAVAILABLE("SYSTEM_002", "Service temporarily unavailable"),
    SYSTEM_DATABASE_ERROR("SYSTEM_003", "Database operation failed");
    
    private final String code;
    private final String defaultMessage;
    
    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDefaultMessage() {
        return defaultMessage;
    }
}