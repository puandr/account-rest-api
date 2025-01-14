package com.example.accountservice.dto;

import com.example.accountservice.validation.ValidCurrency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for a deposit request.
 *
 * This class is used to transfer data for depositing money into an account,
 * including the deposit amount and the currency.
 *
 * Annotations:
 * - {@link Data}: Generates boilerplate code such as getters, setters, equals, hashCode, and toString.
 * - {@link NotNull}: Ensures that fields are not null.
 * - {@link DecimalMin}: Validates that the amount is greater than a specified minimum value.
 * - {@link Size}: Validates that the currency code has exactly 3 characters.
 * - {@link ValidCurrency}: Custom validation to ensure the currency code is supported.
 *
 * Fields:
 * - {@code amount}: The amount to be deposited, which must be greater than zero.
 * - {@code currency}: The currency code of the deposit (e.g., "USD"), which must be valid and exactly 3 characters.
 */
@Data
public class WithdrawRequestDto {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    @ValidCurrency
    private String currency;
}
