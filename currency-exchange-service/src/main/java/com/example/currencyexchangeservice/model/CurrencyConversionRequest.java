package com.example.currencyexchangeservice.model;

import lombok.Data;

/**
 * Model representing a request for currency conversion.
 *
 * This class is used to encapsulate the data required for a currency conversion operation,
 * including the source currency, target currency, and the amount to be converted.
 *
 * Annotations:
 * - {@link Data}: Generates boilerplate code such as getters, setters, equals, hashCode, and toString.
 *
 * Fields:
 * - {@code fromCurrency}: The source {@link Currency} for the conversion.
 * - {@code toCurrency}: The target {@link Currency} for the conversion.
 * - {@code amount}: The amount to be converted.
 */
@Data
public class CurrencyConversionRequest {
    private Currency fromCurrency;
    private Currency toCurrency;
    private double amount;
}

