package com.example.currencyexchangeservice.service;

import com.example.currencyexchangeservice.exception.ExchangeRateNotFoundException;
import com.example.currencyexchangeservice.model.Currency;
import com.example.currencyexchangeservice.model.CurrencyConversionRequest;
import com.example.currencyexchangeservice.model.CurrencyConversionResponse;
import com.example.currencyexchangeservice.model.ExchangeRate;
import com.example.currencyexchangeservice.repository.ExchangeRateRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing currency exchange operations.
 *
 * This service provides functionality for:
 * - Fetching all supported currencies.
 * - Converting a specified amount from one currency to another using exchange rates.
 *
 * Key Features:
 * - Validates the input request to ensure supported currencies and valid exchange rates.
 * - Logs significant actions, such as fetching supported currencies and performing conversions.
 * - Throws {@link ExchangeRateNotFoundException} when a required exchange rate or currency is not available.
 *
 * Dependencies:
 * - {@link ExchangeRateRepository}: For retrieving exchange rates and supported currencies.
 *
 * Annotations:
 * - {@link Service}: Marks this class as a Spring service component.
 */
@Service
public class CurrencyExchangeService {

    private static final Logger logger = LogManager.getLogger(CurrencyExchangeService.class);

    private final ExchangeRateRepository exchangeRateRepository;

    public CurrencyExchangeService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    /**
     * Retrieves all currencies supported by the exchange service.
     *
     * The list is derived from the exchange rates available in the repository.
     *
     * @return a {@link List} of {@link Currency} objects representing all supported currencies.
     */
    public List<Currency> getAllSupportedCurrencies() {
        logger.info("Fetching all supported currencies...");
        List<ExchangeRate> rates = exchangeRateRepository.getAllRates();
        return rates.stream()
                .map(ExchangeRate::getFromCurrency)
                .distinct()
                .toList();
    }

    /**
     * Converts a specified amount from one currency to another.
     *
     * Validates the input request to ensure the provided currencies are supported and an exchange rate exists.
     * Calculates the converted amount based on the exchange rate.
     *
     * @param request the {@link CurrencyConversionRequest} containing the source currency, target currency, and amount.
     * @return a {@link CurrencyConversionResponse} containing the target currency and the converted amount.
     * @throws ExchangeRateNotFoundException if the exchange rate or one of the currencies is not supported.
     */
    public CurrencyConversionResponse convertCurrency(CurrencyConversionRequest request) {
        logger.info("Converting {} {} to {}", request.getAmount(), request.getFromCurrency(), request.getToCurrency());

        if (request.getFromCurrency() == request.getToCurrency()) {
            logger.info("Exchange rate not required for the same currency: {}", request.getFromCurrency());
            throw new ExchangeRateNotFoundException(
                    "Exchange rate not required for the same currency: " + request.getFromCurrency()
            );
        }

        List<Currency> supportedCurrencies = getAllSupportedCurrencies();
        if (!supportedCurrencies.contains(request.getFromCurrency()) || !supportedCurrencies.contains(request.getToCurrency())) {
            throw new ExchangeRateNotFoundException(
                    "Unsupported currency provided. Supported currencies are: " + supportedCurrencies
            );
        }

        ExchangeRate rate = exchangeRateRepository.findRate(
                request.getFromCurrency().name(),
                request.getToCurrency().name()
        ).orElseThrow(() -> new ExchangeRateNotFoundException(
                "Exchange rate not found for " + request.getFromCurrency() + " to " + request.getToCurrency()
        ));

        double convertedAmount = request.getAmount() * rate.getSellRate();
        logger.info("Conversion successful: {} {} = {} {}", request.getAmount(), request.getFromCurrency(),
                convertedAmount, request.getToCurrency());

        return new CurrencyConversionResponse(request.getToCurrency(), convertedAmount);
    }
}
