package com.example.currencyexchangeservice.util;

import com.example.currencyexchangeservice.model.ExchangeRate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.InputStream;
import java.util.List;

/**
 * Utility class for loading exchange rates from a JSON file.
 *
 * This class provides a method to read and parse a JSON file containing a list of {@link ExchangeRate} objects.
 * It is designed to handle file loading errors gracefully and log significant events during the loading process.
 *
 * Key Features:
 * - Reads a JSON file from the application's resources.
 * - Converts the file contents into a list of {@link ExchangeRate} objects.
 * - Logs the loading process, including errors and successes.
 *
 * Dependencies:
 * - {@link ObjectMapper}: Used for JSON parsing.
 *
 * Methods:
 * - {@link #loadExchangeRates(String)}: Loads exchange rates from the specified JSON file.
 */
public class JsonFileLoader {

    private static final Logger logger = LogManager.getLogger(JsonFileLoader.class);

    /**
     * Loads exchange rates from a specified JSON file located in the application's resources.
     *
     * This method reads the file, parses its contents into a list of {@link ExchangeRate} objects, and logs the process.
     * If the file is not found or cannot be parsed, an exception is thrown.
     *
     * @param fileName the name of the JSON file to load (e.g., "rates.json").
     * @return a {@link List} of {@link ExchangeRate} objects parsed from the file.
     * @throws IllegalStateException if the file is not found in the resources.
     * @throws RuntimeException if an error occurs during file reading or parsing.
     */
    public static List<ExchangeRate> loadExchangeRates(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = JsonFileLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                logger.error("File {} not found in resources.", fileName);
                throw new IllegalStateException(fileName + " file not found in resources");
            }
            logger.info("Loading exchange rates from file: {}", fileName);
            List<ExchangeRate> rates = objectMapper.readValue(inputStream, new TypeReference<List<ExchangeRate>>() {});
            logger.info("Successfully loaded {} exchange rates from file: {}", rates.size(), fileName);
            return rates;
        } catch (Exception e) {
            logger.error("Failed to load exchange rates from file: {}", fileName, e);
            throw new RuntimeException("Failed to load data from " + fileName, e);
        }
    }
}
