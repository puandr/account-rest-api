package com.example.accountservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
/**
 * Data Transfer Object (DTO) for a currency exchange request.
 *
 * This class is used to transfer data for initiating a currency exchange operation,
 * including the source currency, target currency, and the amount to be exchanged.
 *
 * Annotations:
 * - {@link Data}: Generates boilerplate code such as getters, setters, equals, hashCode, and toString.
 * - {@link NotNull}: Validates that fields are not null, ensuring required inputs are provided.
 * - {@link DecimalMin}: Ensures the amount is greater than or equal to the specified minimum value.
 *
 * Fields:
 * - {@code fromCurrency}: The source currency code for the exchange (e.g., "USD").
 * - {@code toCurrency}: The target currency code for the exchange (e.g., "EUR").
 * - {@code amount}: The amount to be exchanged, which must be greater than zero.
 */
@Data
public class CurrencyExchangeRequestDto {

    @NotNull(message = "Source currency is required")
    private String fromCurrency;

    @NotNull(message = "Target currency is required")
    private String toCurrency;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;
}
