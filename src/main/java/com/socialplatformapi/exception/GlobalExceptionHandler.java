package com.socialplatformapi.exception;

import com.socialplatformapi.exception.auth.AuthenticationException;
import com.socialplatformapi.exception.comment.CommentException;
import com.socialplatformapi.exception.like.LikeException;
import com.socialplatformapi.exception.post.PostException;
import com.socialplatformapi.exception.user.RegistrationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Build a standardized error response with all required fields
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String errorCode,
            String message,
            HttpServletRequest request,
            Map<String, String> fieldErrors,
            Map<String, Object> details) {
        
        String requestId = generateRequestId();
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .errorCode(errorCode)
                .message(message)
                .endpoint(request.getRequestURI())
                .requestId(requestId)
                .fieldErrors(fieldErrors)
                .details(details)
                .build();

        // Log the error for monitoring and debugging
        if (status.is5xxServerError()) {
            log.error("Server error occurred - RequestId: {}, Endpoint: {}, Error: {}", 
                     requestId, request.getRequestURI(), message);
        } else if (status.is4xxClientError()) {
            log.warn("Client error occurred - RequestId: {}, Endpoint: {}, Error: {}", 
                     requestId, request.getRequestURI(), message);
        }

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Build error response for BaseException instances
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(BaseException ex, HttpServletRequest request) {
        return buildErrorResponse(
            ex.getHttpStatus(),
            ex.getErrorCode(),
            ex.getMessage(),
            request,
            null,
            null
        );
    }

    /**
     * Build simple error response without field errors or details
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status, 
            String errorCode, 
            String message, 
            HttpServletRequest request) {
        return buildErrorResponse(status, errorCode, message, request, null, null);
    }

    /**
     * Generate unique request ID for tracing
     */
    private String generateRequestId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Handle all custom exceptions that extend BaseException
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, HttpServletRequest request) {
        log.debug("BaseException caught: {} - {}", ex.getErrorCode(), ex.getMessage());
        return buildErrorResponse(ex, request);
    }

    /**
     * Handle authentication-related exceptions
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthenticationException ex, HttpServletRequest request) {
        log.warn("Authentication failed: {} - {}", ex.getErrorCode(), ex.getMessage());
        return buildErrorResponse(ex, request);
    }

    /**
     * Handle comment-related exceptions
     */
    @ExceptionHandler(CommentException.class)
    public ResponseEntity<ErrorResponse> handleCommentException(CommentException ex, HttpServletRequest request) {
        log.debug("Comment operation failed: {} - {}", ex.getErrorCode(), ex.getMessage());
        return buildErrorResponse(ex, request);
    }

    /**
     * Handle post-related exceptions
     */
    @ExceptionHandler(PostException.class)
    public ResponseEntity<ErrorResponse> handlePostException(PostException ex, HttpServletRequest request) {
        log.debug("Post operation failed: {} - {}", ex.getErrorCode(), ex.getMessage());
        return buildErrorResponse(ex, request);
    }

    /**
     * Handle user registration exceptions
     */
    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ErrorResponse> handleRegistrationException(RegistrationException ex, HttpServletRequest request) {
        log.info("User registration failed: {} - {}", ex.getErrorCode(), ex.getMessage());
        return buildErrorResponse(ex, request);
    }

    /**
     * Handle like-related exceptions
     */
    @ExceptionHandler(LikeException.class)
    public ResponseEntity<ErrorResponse> handleLikeException(LikeException ex, HttpServletRequest request) {
        log.debug("Like operation failed: {} - {}", ex.getErrorCode(), ex.getMessage());
        return buildErrorResponse(ex, request);
    }

    /**
     * Handle validation errors for request body validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        log.debug("Validation failed for request: {} with {} field errors", request.getRequestURI(), fieldErrors.size());
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST, 
            ErrorCode.VALIDATION_FAILED.getCode(),
            "Input validation failed", 
            request, 
            fieldErrors, 
            null
        );
    }

    /**
     * Handle constraint validation violations
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> violations = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                violations.put(violation.getPropertyPath().toString(), violation.getMessage())
        );

        log.debug("Constraint violations found: {}", violations.size());
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST, 
            ErrorCode.VALIDATION_CONSTRAINT_VIOLATION.getCode(),
            "Constraint validation failed", 
            request, 
            violations, 
            null
        );
    }

    /**
     * Handle malformed JSON requests
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJson(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.debug("Malformed JSON request: {}", ex.getMessage());
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            ErrorCode.VALIDATION_FAILED.getCode(),
            "Malformed JSON request",
            request
        );
    }

    /**
     * Handle missing required parameters
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.debug("Missing required parameter: {}", ex.getParameterName());
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            ErrorCode.VALIDATION_FAILED.getCode(),
            "Missing required parameter: " + ex.getParameterName(),
            request
        );
    }

    /**
     * Handle invalid method arguments (type mismatches)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.debug("Type mismatch for parameter: {}", ex.getName());
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            ErrorCode.VALIDATION_FAILED.getCode(),
            "Invalid parameter type for: " + ex.getName(),
            request
        );
    }

    /**
     * Handle unsupported HTTP methods
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.debug("Method not supported: {}", ex.getMethod());
        return buildErrorResponse(
            HttpStatus.METHOD_NOT_ALLOWED,
            "METHOD_NOT_ALLOWED",
            "HTTP method '" + ex.getMethod() + "' is not supported for this endpoint",
            request
        );
    }

    /**
     * Handle endpoint not found
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException ex, HttpServletRequest request) {
        log.debug("Endpoint not found: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        return buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "ENDPOINT_NOT_FOUND",
            "Endpoint not found",
            request
        );
    }

    /**
     * Handle database constraint violations
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.error("Database constraint violation: {}", ex.getMessage());
        return buildErrorResponse(
            HttpStatus.CONFLICT,
            ErrorCode.SYSTEM_DATABASE_ERROR.getCode(),
            "Database constraint violation occurred",
            request
        );
    }

    /**
     * Handle all other unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllOtherExceptions(Exception ex, HttpServletRequest request) {
        log.error("Unexpected exception occurred", ex);
        return buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            ErrorCode.SYSTEM_INTERNAL_ERROR.getCode(),
            "An unexpected error occurred", 
            request
        );
    }
}
