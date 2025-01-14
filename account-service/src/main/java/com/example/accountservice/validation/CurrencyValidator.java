package com.example.accountservice.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Custom validator to check if a given currency is valid based on a predefined list of supported currencies.
 *
 * This validator:
 * - Loads the list of supported currencies from a JSON file at initialization.
 * - Validates whether a provided currency is included in the supported currencies.
 * - Customizes validation messages for unsupported currencies.
 *
 * Dependencies:
 * - {@link ResourceLoader}: To load the JSON file containing the supported currencies.
 * - {@link ObjectMapper}: To parse the JSON file into a usable format.
 *
 * Exceptions:
 * - {@link IllegalStateException}: Thrown if the JSON file cannot be loaded or parsed.
 */
@Component
public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    private final ResourceLoader resourceLoader;
    private List<String> supportedCurrencies;

    public CurrencyValidator(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Initializes the validator by loading the list of supported currencies from a JSON file.
     *
     * @param constraintAnnotation the annotation instance for additional metadata (not used in this implementation).
     * @throws IllegalStateException if the JSON file cannot be loaded or parsed.
     */
    @Override
    public void initialize(ValidCurrency constraintAnnotation) {
        Resource resource = resourceLoader.getResource("classpath:supportedCurrencies.json");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, List<String>> jsonMap = objectMapper.readValue(resource.getInputStream(), Map.class);
            supportedCurrencies = jsonMap.get("supportedCurrencies");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load supported currencies from file", e);
        }
    }

    /**
     * Validates if the given currency value is included in the supported currencies.
     *
     * @param value the currency value to validate.
     * @param context the {@link ConstraintValidatorContext} used to customize validation messages.
     * @return {@code true} if the currency is valid, {@code false} otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        boolean isValid = supportedCurrencies.contains(value);
        if (!isValid) {
            // Customize the validation message
            String message = "Unsupported currency. Supported currencies are: " + String.join(", ", supportedCurrencies);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }

        return isValid;
    }
}
