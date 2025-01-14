package com.example.currencyexchangeservice.model;

import lombok.Data;

/**
 * Model representing the response for a currency conversion operation.
 *
 * This class encapsulates the details of the conversion result, including the target currency
 * and the converted amount.
 *
 * Annotations:
 * - {@link Data}: Generates boilerplate code such as getters, setters, equals, hashCode, and toString.
 *
 * Fields:
 * - {@code toCurrency}: The target {@link Currency} after the conversion.
 * - {@code convertedAmount}: The amount converted to the target currency.
 *
 * Constructor:
 * - {@link #CurrencyConversionResponse(Currency, double)}: Initializes the response with the target currency and converted amount.
 */
@Data
public class CurrencyConversionResponse {
    private Currency toCurrency;
    private double convertedAmount;

    public CurrencyConversionResponse(Currency toCurrency, double convertedAmount) {
        this.toCurrency = toCurrency;
        this.convertedAmount = convertedAmount;
    }
}

