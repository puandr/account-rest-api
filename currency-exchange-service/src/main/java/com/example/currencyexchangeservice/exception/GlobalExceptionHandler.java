package com.example.currencyexchangeservice.exception;

import com.example.currencyexchangeservice.service.CurrencyExchangeService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for the Currency Exchange Service.
 *
 * This class centralizes exception handling across the application, providing custom responses
 * for specific exceptions. It ensures a consistent structure for error messages and helps manage
 * application-specific exceptions and common exceptions like {@link InvalidFormatException}.
 *
 * Key Features:
 * - Handles custom exceptions like {@link ExchangeRateNotFoundException}.
 * - Provides user-friendly error messages for invalid input formats.
 * - Logs errors for debugging and troubleshooting.
 *
 * Dependencies:
 * - {@link CurrencyExchangeService}: Used to retrieve supported currencies for error responses.
 *
 * Annotations:
 * - {@link RestControllerAdvice}: Marks this class as a global exception handler for REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final CurrencyExchangeService currencyExchangeService;

    public GlobalExceptionHandler(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }

    /**
     * Handles {@link ExchangeRateNotFoundException}.
     *
     * This method returns a structured error response with a 400 Bad Request status
     * when an exchange rate for the requested currency pair is not found.
     *
     * @param ex the {@link ExchangeRateNotFoundException} thrown during currency exchange operations.
     * @return a {@link ResponseEntity} containing a structured error message.
     */
    @ExceptionHandler(ExchangeRateNotFoundException.class)
    public ResponseEntity<Object> handleExchangeRateNotFoundException(ExchangeRateNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link InvalidFormatException}.
     *
     * This method returns a structured error response with a 400 Bad Request status
     * when a provided value does not match the expected format, such as an unsupported currency.
     * The response includes a list of supported currencies.
     *
     * @param ex the {@link InvalidFormatException} thrown during deserialization of request data.
     * @return a {@link ResponseEntity} containing a structured error message.
     */
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex) {
        String supportedCurrencies = currencyExchangeService.getAllSupportedCurrencies().stream()
                .map(Enum::name)
                .collect(Collectors.joining(", "));
        
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", "Invalid currency value provided. Supported currencies are: " + supportedCurrencies);


        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
