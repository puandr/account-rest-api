package com.example.accountservice.service.impl;

import com.example.accountservice.dto.CurrencyConversionResponseDto;
import com.example.accountservice.exception.CurrencyExchangeException;
import com.example.accountservice.service.CurrencyExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Implementation of the {@link CurrencyExchangeService} interface for handling currency conversion operations.
 *
 * This service interacts with an external currency exchange service to convert amounts between different currencies.
 * It uses a {@link RestTemplate} to perform HTTP requests and handle responses.
 *
 * Responsibilities:
 * - Convert a specified amount from one currency to another using an external service.
 * - Handle errors gracefully and log issues for troubleshooting.
 *
 * Dependencies:
 * - {@link RestTemplate}: For making HTTP requests to the external currency exchange service.
 *
 * Exceptions:
 * - {@link CurrencyExchangeException}: Thrown when any error occurs during the currency conversion process.
 */
@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CurrencyExchangeServiceImpl.class);


    public CurrencyExchangeServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Converts a specified amount from one currency to another using an external service.
     *
     * @param fromCurrency the currency to convert from (e.g., "USD").
     * @param toCurrency the currency to convert to (e.g., "EUR").
     * @param amount the amount of money to convert.
     * @return a {@link CurrencyConversionResponseDto} containing the conversion details (e.g., converted amount).
     * @throws CurrencyExchangeException if an error occurs during the currency conversion process, such as:
     *         - External service being unavailable ({@link ResourceAccessException}).
     *         - Client or server-side HTTP errors ({@link HttpClientErrorException} or {@link HttpServerErrorException}).
     *         - Any unexpected errors during the process.
     */
    @Override
    public CurrencyConversionResponseDto convert(String fromCurrency, String toCurrency, BigDecimal amount) {
        String url = "http://currency-exchange-service:8081/api/convert";
        Map<String, Object> request = Map.of(
                "fromCurrency", fromCurrency,
                "toCurrency", toCurrency,
                "amount", amount
        );

        try {
            return restTemplate.postForObject(url, request, CurrencyConversionResponseDto.class);
        } catch (ResourceAccessException ex) {
            logger.error("Currency exchange service is unavailable: {}", ex.getMessage());
            throw new CurrencyExchangeException("Currency exchange service is unavailable", ex);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error during currency conversion: {}", ex.getResponseBodyAsString());
            throw new CurrencyExchangeException("Error during currency conversion: " + ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            logger.error("Unexpected error during currency conversion", ex);
            throw new CurrencyExchangeException("Unexpected error during currency conversion", ex);
        }
    }

}
