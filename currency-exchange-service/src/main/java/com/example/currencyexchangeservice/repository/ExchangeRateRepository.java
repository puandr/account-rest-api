package com.example.currencyexchangeservice.repository;

import com.example.currencyexchangeservice.model.ExchangeRate;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing exchange rates.
 *
 * This interface defines the contract for accessing and retrieving exchange rate data.
 * Implementations of this interface are responsible for providing the data source integration,
 * such as a database or an external service.
 *
 * Methods:
 * - {@link #getAllRates()}: Retrieves all available exchange rates.
 * - {@link #findRate(String, String)}: Finds a specific exchange rate for a given currency pair.
 *
 * Fields:
 * - None. This is an interface defining the contract for implementations.
 */
public interface ExchangeRateRepository {
    List<ExchangeRate> getAllRates();
    Optional<ExchangeRate> findRate(String fromCurrency, String toCurrency);
}
