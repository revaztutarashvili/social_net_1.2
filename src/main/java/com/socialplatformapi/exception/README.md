# Exception Handling სისტემა

## მიმოხილვა

ეს პროექტი იყენებს სტანდარტიზებულ exception handling სისტემას, რომელიც უზრუნველყოფს:

- **სტანდარტიზებულ error response-ებს** ყველა API endpoint-ისთვის
- **ერთიან error code-ებს** მარტივი error იდენტიფიცირებისთვის
- **სტრუქტურირებულ logging-ს** monitoring და debugging-ისთვის
- **Type-safe HTTP status code-ებს** თითოეული exception ტიპისთვის

## არქიტექტურა

### BaseException
ყველა custom exception-ის მშობელი კლასი, რომელიც უზრუნველყოფს:
- Error code management
- HTTP status code mapping
- Message handling with parameters

### ErrorCode Enum
Centralized error code definitions with:
```java
ErrorCode.AUTH_INVALID_CREDENTIALS  // "AUTH_003"
ErrorCode.POST_NOT_FOUND           // "POST_001"
ErrorCode.USER_USERNAME_EXISTS     // "USER_001"
```

### ErrorResponse DTO
სტანდარტიზებული API response:
```json
{
  "timestamp": "2024-01-20T10:30:45",
  "status": 404,
  "error": "Not Found",
  "errorCode": "POST_001", 
  "message": "Post with id 123 does not exist",
  "endpoint": "/posts/123",
  "requestId": "abc12345",
  "fieldErrors": null,
  "details": null
}
```

## გამოყენება

### Service Layer-ში Exception-ების ყვანა

```java
// ✅ სწორი - ErrorCode-ის გამოყენება
if (!postRepository.existsById(postId)) {
    throw new PostException(ErrorCode.POST_NOT_FOUND, 
        "Post with id " + postId + " does not exist");
}

// ❌ არასწორი - string-ის გამოყენება
throw new PostException("Post not found");
```

### ახალი Exception ტიპის შექმნა

1. **ErrorCode-ის დამატება**:
```java
// ErrorCode.java-ში
NOTIFICATION_NOT_FOUND("NOTIFICATION_001", "Notification not found"),
NOTIFICATION_ALREADY_READ("NOTIFICATION_002", "Notification already marked as read")
```

2. **Exception კლასის შექმნა**:
```java
public class NotificationException extends BaseException {
    public NotificationException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getDefaultMessage(), HttpStatus.NOT_FOUND);
    }
    
    public NotificationException(ErrorCode errorCode, String customMessage) {
        super(errorCode.getCode(), customMessage, HttpStatus.NOT_FOUND);
    }
}
```

3. **GlobalExceptionHandler-ში Handler-ის დამატება**:
```java
@ExceptionHandler(NotificationException.class)
public ResponseEntity<ErrorResponse> handleNotificationException(
        NotificationException ex, HttpServletRequest request) {
    log.debug("Notification operation failed: {} - {}", ex.getErrorCode(), ex.getMessage());
    return buildErrorResponse(ex, request);
}
```

## HTTP Status Code Mapping

| Exception ტიპი | Primary Status | ErrorCode-ის მიხედვით |
|---------------|----------------|----------------------|
| AuthenticationException | 401 UNAUTHORIZED | - |
| RegistrationException | 409 CONFLICT | - |  
| PostException | განსხვავებული | POST_NOT_FOUND → 404<br>POST_ACCESS_DENIED → 403<br>POST_ALREADY_LIKED → 409 |
| CommentException | განსხვავებული | COMMENT_NOT_FOUND → 404<br>COMMENT_ACCESS_DENIED → 403 |
| LikeException | განსხვავებული | LIKE_ALREADY_EXISTS → 409<br>LIKE_NOT_FOUND → 404 |

## Validation Error Handling

Spring validation errors ავტომატურად ქმნის:
```json
{
  "errorCode": "VALIDATION_001",
  "message": "Input validation failed", 
  "fieldErrors": {
    "email": "Email is required",
    "username": "Username must be at least 3 characters"
  }
}
```

## Logging

Exception-ები ავტომატურად log-ება:
- **ERROR level**: 5xx server errors
- **WARN level**: Authentication failures
- **DEBUG level**: Business logic errors (4xx client errors)

Request ID იქმნება ყველა error-ისთვის tracing-ისთვის.

## Testing

Exception handler-ების testing example:
```java
@Test
void shouldReturnNotFoundForMissingPost() {
    PostException ex = new PostException(ErrorCode.POST_NOT_FOUND);
    ResponseEntity<ErrorResponse> response = handler.handlePostException(ex, request);
    
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody().getErrorCode()).isEqualTo("POST_001");
}
```

## Best Practices

1. **მუდამ იყენეთ ErrorCode**: არასოდეს მისცეთ string message პირდაპირ
2. **Custom message-ები**: გამოიყენეთ მხოლოდ კონტექსტური ინფორმაციისთვის
3. **Consistent Status Codes**: იყენეთ HTTP status code-ები ErrorCode-ის მიხედვით
4. **Log Context**: დაამატეთ relevant context information log messages-ში
5. **Test Coverage**: დაწერეთ ტესტები ყველა exception scenario-სთვის