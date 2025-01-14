package com.example.currencyexchangeservice.model;

import lombok.Data;

import java.util.List;

/**
 * Model representing the response for supported currencies.
 *
 * This class encapsulates the list of currencies supported by the currency exchange service.
 *
 * Annotations:
 * - {@link Data}: Generates boilerplate code such as getters, setters, equals, hashCode, and toString.
 *
 * Fields:
 * - {@code supportedCurrencies}: A list of {@link Currency} objects representing the supported currencies.
 *
 * Constructor:
 * - {@link #SupportedCurrenciesResponse(List)}: Initializes the response with a list of supported currencies.
 */
@Data
public class SupportedCurrenciesResponse {
    private List<Currency> supportedCurrencies;

    public SupportedCurrenciesResponse(List<Currency> supportedCurrencies) {
        this.supportedCurrencies = supportedCurrencies;
    }
}

