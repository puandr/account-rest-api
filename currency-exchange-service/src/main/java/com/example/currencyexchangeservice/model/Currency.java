package com.example.currencyexchangeservice.model;

//TODO change the logic, so all supported currencies are fetched from file
/**
 * Enum representing the supported currencies in the currency exchange service.
 *
 * This enum is used to define the list of currencies that the service can handle.
 *
 * Values:
 * - {@code EUR}: Euro.
 * - {@code USD}: United States Dollar.
 * - {@code SEK}: Swedish Krona.
 * - {@code RUB}: Russian Ruble.
 */
public enum Currency {
    EUR, USD, SEK, RUB
}
