package com.example.currencyexchangeservice.controller;

import com.example.currencyexchangeservice.model.Currency;
import com.example.currencyexchangeservice.model.CurrencyConversionRequest;
import com.example.currencyexchangeservice.model.CurrencyConversionResponse;
import com.example.currencyexchangeservice.service.CurrencyExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing currency exchange operations.
 *
 * This controller provides endpoints for:
 * - Retrieving the list of supported currencies.
 * - Performing currency conversions based on fixed exchange rates.
 *
 * Key Features:
 * - Integrates with the {@link CurrencyExchangeService} to handle business logic.
 * - Provides Swagger/OpenAPI documentation for API consumers.
 * - Logs incoming requests and responses for auditing and debugging purposes.
 *
 * Annotations:
 * - {@link RestController}: Marks this class as a Spring REST controller.
 * - {@link RequestMapping}: Sets the base path for all endpoints in this controller as `/api`.
 */
@RestController
@RequestMapping("/api")
public class CurrencyExchangeController {

    private static final Logger logger = LogManager.getLogger(CurrencyExchangeController.class);

    private final CurrencyExchangeService currencyExchangeService;

    public CurrencyExchangeController(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }


    /**
     * Retrieves the list of all supported currencies for currency exchange.
     *
     * Logs the request and the number of supported currencies retrieved. The list of supported currencies
     * is determined by the exchange rates available in the system.
     *
     * OpenAPI Documentation:
     * - Summary: "Get Supported Currencies"
     * - Description: "Retrieve a list of all currencies that are supported for currency exchange."
     * - Response:
     *   - {@code 200}: Successfully retrieves the list of supported currencies.
     *
     * @return a {@link List} of {@link Currency} objects representing supported currencies.
     */
    @Operation(
            summary = "Get Supported Currencies",
            description = "Retrieve a list of all currencies that are supported for currency exchange.",
            tags = {"Currency Exchange"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of supported currencies retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Currency.class))
                    )
            )
    })
    @GetMapping("/currencies")
    public List<Currency> getSupportedCurrencies() {
        logger.info("Received request to fetch all supported currencies.");
        List<Currency> currencies = currencyExchangeService.getAllSupportedCurrencies();
        logger.info("Returning {} supported currencies.", currencies.size());
        return currencies;
    }

    /**
     * Converts a specified amount from one currency to another based on fixed exchange rates.
     *
     * Validates the input request and retrieves the conversion result using the {@link CurrencyExchangeService}.
     * Logs the request and response for auditing purposes.
     *
     * OpenAPI Documentation:
     * - Summary: "Convert Currency"
     * - Description: "Convert an amount from one currency to another based on fixed exchange rates."
     * - Responses:
     *   - {@code 200}: Conversion successful, returns the converted amount.
     *   - {@code 400}: Invalid input or unsupported currency provided.
     *   - {@code 500}: Unexpected error occurred during the conversion.
     *
     * @param request the {@link CurrencyConversionRequest} containing the source currency, target currency, and amount to convert.
     * @return a {@link CurrencyConversionResponse} containing the target currency and the converted amount.
     */
    @Operation(
            summary = "Convert Currency",
            description = "Convert an amount from one currency to another based on fixed exchange rates.",
            tags = {"Currency Exchange"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Currency converted successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CurrencyConversionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or unsupported currency provided.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "An unexpected error occurred during the conversion.",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/convert")
    public CurrencyConversionResponse convertCurrency(@RequestBody CurrencyConversionRequest request) {
        logger.info("Received currency conversion request: {}", request);
        CurrencyConversionResponse response = currencyExchangeService.convertCurrency(request);
        logger.info("Conversion result: {}", response);
        return response;
    }
}
