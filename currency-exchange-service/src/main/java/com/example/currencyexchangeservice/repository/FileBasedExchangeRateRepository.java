package com.example.currencyexchangeservice.repository;

import com.example.currencyexchangeservice.model.ExchangeRate;
import com.example.currencyexchangeservice.util.JsonFileLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * File-based implementation of the {@link ExchangeRateRepository} interface.
 *
 * This repository loads exchange rates from a JSON file at initialization and provides
 * methods to retrieve all rates or find a specific rate for a currency pair.
 *
 * Key Features:
 * - Loads exchange rates from a file (`rates.json`) using {@link JsonFileLoader}.
 * - Provides a thread-safe way to retrieve exchange rates.
 * - Logs significant events, such as file loading and errors, for debugging and monitoring.
 *
 * Dependencies:
 * - {@link JsonFileLoader}: Utility class for loading exchange rates from a JSON file.
 *
 * Annotations:
 * - {@link Repository}: Marks this class as a Spring-managed repository component.
 *
 * Fields:
 * - {@code exchangeRates}: A list of exchange rates loaded from the file.
 *
 * Constructor:
 * - {@link #FileBasedExchangeRateRepository()}: Loads exchange rates from the file during initialization.
 *
 * Methods:
 * - {@link #getAllRates()}: Retrieves all loaded exchange rates.
 * - {@link #findRate(String, String)}: Finds an exchange rate for a specific currency pair.
 */
@Repository
public class FileBasedExchangeRateRepository implements ExchangeRateRepository {

    private static final Logger logger = LogManager.getLogger(FileBasedExchangeRateRepository.class);

    private final List<ExchangeRate> exchangeRates = new ArrayList<>();

    /**
     * Default constructor for {@code FileBasedExchangeRateRepository}.
     *
     * Initializes the repository by loading exchange rates from a file. Logs success or failure
     * during the initialization process.
     *
     * @throws RuntimeException if the exchange rates cannot be loaded from the file.
     */
    public FileBasedExchangeRateRepository() {
        try {
            loadRatesFromFile();
            logger.info("Successfully loaded exchange rates from file.");
        } catch (Exception e) {
            logger.error("Failed to load exchange rates during initialization.", e);
            throw new RuntimeException("Error initializing FileBasedExchangeRateRepository", e);
        }
    }

    /**
     * Loads exchange rates from a JSON file and stores them in the {@code exchangeRates} list.
     *
     * The file path is hardcoded as `rates.json`. Logs the process of loading rates and handles any errors.
     */
    private void loadRatesFromFile() {
        logger.info("Loading exchange rates from file...");
        exchangeRates.addAll(JsonFileLoader.loadExchangeRates("rates.json"));
    }

    /**
     * Retrieves all exchange rates loaded from the file.
     *
     * @return a new {@link List} containing all loaded {@link ExchangeRate} objects.
     */
    @Override
    public List<ExchangeRate> getAllRates() {
        return new ArrayList<>(exchangeRates);
    }

    /**
     * Finds the exchange rate for a specific currency pair.
     *
     * Searches the loaded exchange rates for a match based on the source and target currencies.
     * Logs the search process for debugging.
     *
     * @param fromCurrency the source currency code (e.g., "USD").
     * @param toCurrency the target currency code (e.g., "EUR").
     * @return an {@link Optional} containing the matching {@link ExchangeRate} if found,
     *         or an empty {@link Optional} if no match exists.
     */
    @Override
    public Optional<ExchangeRate> findRate(String fromCurrency, String toCurrency) {
        logger.debug("Finding rate from {} to {}", fromCurrency, toCurrency);
        return exchangeRates.stream()
                .filter(rate -> rate.getFromCurrency().name().equalsIgnoreCase(fromCurrency)
                        && rate.getToCurrency().name().equalsIgnoreCase(toCurrency))
                .findFirst();
    }
}
