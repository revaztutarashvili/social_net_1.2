package com.socialplatformapi.exception;

import com.socialplatformapi.exception.auth.AuthenticationException;
import com.socialplatformapi.exception.post.PostException;
import com.socialplatformapi.exception.user.RegistrationException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private MethodArgumentNotValidException validationException;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn("/test-endpoint");
    }

    @Test
    void handleAuthException_ShouldReturnUnauthorizedWithErrorCode() {
        // Given
        AuthenticationException ex = new AuthenticationException(ErrorCode.AUTH_INVALID_CREDENTIALS);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAuthException(ex, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo(ErrorCode.AUTH_INVALID_CREDENTIALS.getCode());
        assertThat(response.getBody().getMessage()).isEqualTo(ErrorCode.AUTH_INVALID_CREDENTIALS.getDefaultMessage());
        assertThat(response.getBody().getEndpoint()).isEqualTo("/test-endpoint");
    }

    @Test
    void handleRegistrationException_ShouldReturnConflictWithErrorCode() {
        // Given
        RegistrationException ex = new RegistrationException(ErrorCode.USER_USERNAME_EXISTS, "Custom username error");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleRegistrationException(ex, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo(ErrorCode.USER_USERNAME_EXISTS.getCode());
        assertThat(response.getBody().getMessage()).isEqualTo("Custom username error");
    }

    @Test
    void handlePostException_ShouldReturnCorrectStatusBasedOnErrorCode() {
        // Given
        PostException notFoundEx = new PostException(ErrorCode.POST_NOT_FOUND);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlePostException(notFoundEx, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorCode()).isEqualTo(ErrorCode.POST_NOT_FOUND.getCode());
    }

    @Test
    void handleValidationException_ShouldReturnBadRequestWithFieldErrors() {
        // Given
        List<FieldError> fieldErrors = Arrays.asList(
            new FieldError("userRequest", "email", "Email is required"),
            new FieldError("userRequest", "username", "Username must be at least 3 characters")
        );
        
        when(validationException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(validationException, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo(ErrorCode.VALIDATION_FAILED.getCode());
        assertThat(response.getBody().getFieldErrors()).hasSize(2);
        assertThat(response.getBody().getFieldErrors()).containsEntry("email", "Email is required");
        assertThat(response.getBody().getFieldErrors()).containsEntry("username", "Username must be at least 3 characters");
    }

    @Test
    void handleAllOtherExceptions_ShouldReturnInternalServerError() {
        // Given
        RuntimeException ex = new RuntimeException("Unexpected error");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAllOtherExceptions(ex, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo(ErrorCode.SYSTEM_INTERNAL_ERROR.getCode());
        assertThat(response.getBody().getMessage()).isEqualTo("An unexpected error occurred");
        assertThat(response.getBody().getRequestId()).isNotNull();
    }
}