package com.example.accountservice.dto;

import com.example.accountservice.validation.ValidCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing the balance of a specific currency.
 *
 * This class is used to transfer balance data between layers of the application and external clients.
 * It includes the currency code and the corresponding balance amount.
 *
 * Annotations:
 * - {@link Data}: Generates boilerplate code such as getters, setters, equals, hashCode, and toString.
 * - {@link AllArgsConstructor}: Generates a constructor with all fields.
 * - {@link ValidCurrency}: Ensures the currency field contains a valid currency code.
 *
 * Fields:
 * - {@code currency}: The currency code (e.g., "USD", "EUR").
 * - {@code amount}: The balance amount in the specified currency.
 */
@Data
@AllArgsConstructor
public class BalanceResponseDto {
    @ValidCurrency
    private String currency;
    private BigDecimal amount;
}
