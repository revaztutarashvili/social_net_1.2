package com.socialplatformapi.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standardized error response structure for the API.
 * Provides comprehensive error information including error codes, 
 * timestamps, and optional field-level validation errors.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    /**
     * Timestamp when the error occurred
     */
    private LocalDateTime timestamp;
    
    /**
     * HTTP status code
     */
    private int status;
    
    /**
     * HTTP status reason phrase
     */
    private String error;
    
    /**
     * Application-specific error code for easier error identification
     */
    private String errorCode;
    
    /**
     * Human-readable error message
     */
    private String message;
    
    /**
     * API endpoint where the error occurred
     */
    private String endpoint;
    
    /**
     * Request ID for tracing (optional)
     */
    private String requestId;
    
    /**
     * Field-level validation errors (for validation failures)
     */
    private Map<String, String> fieldErrors;
    
    /**
     * Additional context information (optional)
     */
    private Map<String, Object> details;
}
