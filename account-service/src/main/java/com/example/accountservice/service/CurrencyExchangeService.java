package com.example.accountservice.service;

import com.example.accountservice.dto.CurrencyConversionResponseDto;
import java.math.BigDecimal;

/**
 * Interface defining the contract for currency exchange operations.
 *
 * This service provides functionality to:
 * - Convert a specified amount from one currency to another.
 *
 * Implementing classes must handle interactions with external currency exchange services and manage exceptions.
 */
public interface CurrencyExchangeService {
    /**
     * Converts a specified amount from one currency to another.
     *
     * @param fromCurrency the currency to convert from (e.g., "USD").
     * @param toCurrency the currency to convert to (e.g., "EUR").
     * @param amount the amount of money to convert.
     * @return a {@link CurrencyConversionResponseDto} containing the conversion details, including the converted amount.
     * @throws IllegalArgumentException if the input parameters are invalid (e.g., null or negative values).
     * @throws RuntimeException if an error occurs during the conversion process (specific exceptions may be defined in implementations).
     */
    CurrencyConversionResponseDto convert(String fromCurrency, String toCurrency, BigDecimal amount);
}
