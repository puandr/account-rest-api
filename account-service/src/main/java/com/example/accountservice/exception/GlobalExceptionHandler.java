package com.example.accountservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Global exception handler for the Account Service application.
 *
 * This class uses Spring's {@link ControllerAdvice} annotation to intercept and handle exceptions
 * across the application in a centralized manner. It ensures that appropriate HTTP responses are
 * returned for different exceptions, providing a consistent structure for error messages.
 *
 * Key Features:
 * - Handles specific exceptions with tailored HTTP status codes and messages.
 * - Logs exceptions for auditing and debugging purposes.
 * - Provides detailed validation error responses for requests that fail validation.
 *
 * Annotations:
 * - {@link ControllerAdvice}: Marks this class as a global exception handler.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles {@link CurrencyExchangeException}.
     *
     * Logs the exception and returns a 500 Internal Server Error response with the exception message.
     *
     * @param ex the {@link CurrencyExchangeException} thrown during currency exchange operations.
     * @return a {@link ResponseEntity} containing the error details.
     */
    @ExceptionHandler(CurrencyExchangeException.class)
    public ResponseEntity<Map<String, Object>> handleCurrencyExchangeException(CurrencyExchangeException ex) {
        logger.error("CurrencyExchangeException caught: {}", ex.getMessage());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /**
     * Handles application-level {@link EntityNotFoundException}.
     *
     * Returns a 404 Not Found response with the exception message.
     *
     * @param ex the {@link EntityNotFoundException} thrown when an entity is not found.
     * @return a {@link ResponseEntity} containing the error details.
     */
    @ExceptionHandler(com.example.accountservice.exception.EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles JPA's {@link jakarta.persistence.EntityNotFoundException}.
     *
     * Returns a 404 Not Found response with the exception message.
     *
     * @param ex the {@link jakarta.persistence.EntityNotFoundException} thrown for JPA entity not found cases.
     * @return a {@link ResponseEntity} containing the error details.
     */
    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleJpaEntityNotFoundException(jakarta.persistence.EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles {@link InsufficientFundsException}.
     *
     * Returns a 422 Unprocessable Entity response with the exception message.
     *
     * @param ex the {@link InsufficientFundsException} thrown when an account has insufficient funds.
     * @return a {@link ResponseEntity} containing the error details.
     */
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientFundsException(InsufficientFundsException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    /**
     * Handles {@link AccessDeniedException}.
     *
     * Returns a 403 Forbidden response with the exception message.
     *
     * @param ex the {@link AccessDeniedException} thrown for unauthorized access attempts.
     * @return a {@link ResponseEntity} containing the error details.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    /**
     * Handles all unhandled exceptions.
     *
     * Logs the exception and returns a 500 Internal Server Error response with a generic error message.
     *
     * @param ex the {@link Exception} that was not explicitly handled by other methods.
     * @return a {@link ResponseEntity} containing the error details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        logger.error("GlobalExceptionHandler - Unhandled exception: {}", ex.getClass().getName(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    /**
     * Handles validation exceptions thrown by {@link MethodArgumentNotValidException}.
     *
     * Constructs a detailed error response containing field-specific validation error messages.
     *
     * @param ex the {@link MethodArgumentNotValidException} thrown when a request fails validation.
     * @return a {@link ResponseEntity} containing the validation error details.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", error.getDefaultMessage()
                ))
                .toList();

        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "message", "Validation failed",
                "errors", errors
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Helper method to build a standardized error response.
     *
     * @param status the {@link HttpStatus} to be returned.
     * @param message the error message to include in the response.
     * @return a {@link ResponseEntity} containing the error details.
     */
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        );
        return new ResponseEntity<>(body, status);
    }
}
