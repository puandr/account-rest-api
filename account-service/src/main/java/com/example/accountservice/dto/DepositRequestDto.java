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
 * This class is used to encapsulate the data required to perform a deposit operation,
 * including the deposit amount and currency. It includes validation annotations to ensure
 * the input data meets the required criteria.
 *
 * Annotations:
 * - {@link Data}: Automatically generates getters, setters, equals, hashCode, and toString methods.
 * - {@link NotNull}: Ensures that fields are not null.
 * - {@link DecimalMin}: Validates that the deposit amount is greater than the specified minimum value.
 * - {@link Size}: Validates the length of the currency code.
 * - {@link ValidCurrency}: Custom validation annotation to ensure the currency code is valid and supported.
 *
 * Fields:
 * - {@code amount}: The amount to be deposited, which must be greater than zero.
 * - {@code currency}: The 3-character ISO currency code representing the deposit currency (e.g., "USD").
 */
@Data
public class DepositRequestDto {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    @ValidCurrency
    private String currency;
}
