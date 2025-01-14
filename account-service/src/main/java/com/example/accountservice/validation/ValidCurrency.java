package com.example.accountservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom validation annotation to ensure that a field contains a valid currency.
 *
 * This annotation:
 * - Uses the {@link CurrencyValidator} class to perform the validation logic.
 * - Validates that a currency is included in a predefined list of supported currencies.
 * - Can be applied to fields of type {@link String}.
 *
 * Usage:
 * Apply this annotation to a field to enforce currency validation. For example:
 * <pre>
 * {@code
 * @ValidCurrency
 * private String currency;
 * }
 * </pre>
 *
 * Attributes:
 * - {@code message}: Customizes the validation message for invalid currencies.
 * - {@code groups}: Used for grouping constraints (default is empty).
 * - {@code payload}: Used to carry additional information about the validation failure (default is empty).
 *
 * @see CurrencyValidator
 */
@Constraint(validatedBy = CurrencyValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCurrency {
    String message() default "Unsupported currency";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

