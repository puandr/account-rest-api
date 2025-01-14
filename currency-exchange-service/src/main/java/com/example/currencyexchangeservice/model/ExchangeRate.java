package com.example.currencyexchangeservice.model;

import lombok.Data;

/**
 * Model representing an exchange rate between two currencies.
 *
 * This class encapsulates the exchange rates for buying and selling a specific currency pair.
 *
 * Annotations:
 * - {@link Data}: Generates boilerplate code such as getters, setters, equals, hashCode, and toString.
 *
 * Fields:
 * - {@code fromCurrency}: The source {@link Currency} of the exchange rate.
 * - {@code toCurrency}: The target {@link Currency} of the exchange rate.
 * - {@code buyRate}: The rate for buying the {@code toCurrency} using the {@code fromCurrency}.
 * - {@code sellRate}: The rate for selling the {@code toCurrency} to the {@code fromCurrency}.
 *
 * Constructors:
 * - {@link #ExchangeRate()}: Default no-argument constructor.
 * - {@link #ExchangeRate(Currency, Currency, double, double)}: Initializes the exchange rate with the specified currencies and rates.
 */
@Data
public class ExchangeRate {
    private Currency fromCurrency;
    private Currency toCurrency;
    private double buyRate;  // Rate for buying 'toCurrency' using 'fromCurrency'
    private double sellRate; // Rate for selling 'toCurrency' to 'fromCurrency'

    public ExchangeRate() {
    }

    public ExchangeRate(Currency fromCurrency, Currency toCurrency, double buyRate, double sellRate) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
    }
}
