package com.example.accountservice.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing the response of a currency conversion operation.
 *
 * This class is used to transfer data about the result of a currency conversion,
 * including the target currency and the converted amount.
 *
 * Annotations:
 * - {@link Data}: Generates boilerplate code such as getters, setters, equals, hashCode, and toString.
 *
 * Fields:
 * - {@code toCurrency}: The target currency code after conversion (e.g., "EUR").
 * - {@code convertedAmount}: The amount converted to the target currency.
 */
@Data
public class CurrencyConversionResponseDto {
    private String toCurrency;
    private BigDecimal convertedAmount;
}
