package com.example.accountservice.service;

import com.example.accountservice.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Interface defining the contract for account-related operations in the banking application.
 *
 * This service provides the following functionality:
 * - Deposit money into an account in a specific currency.
 * - Withdraw money from an account in a specific currency.
 * - Retrieve balances for all currencies associated with an account.
 * - Exchange currencies within an account.
 *
 * Implementing classes must provide the logic for managing accounts, balances, and transactions.
 */
public interface AccountService {

    /**
     * Deposits a specified amount of money into an account in a given currency.
     *
     * @param accountId the ID of the account to deposit into.
     * @param amount the amount of money to deposit (must be greater than zero).
     * @param currency the currency of the deposit (e.g., "USD", "EUR").
     * @param username the username of the person performing the deposit (for authorization checks).
     * @return the {@link Transaction} representing the deposit operation.
     */
    Transaction deposit(Long accountId, BigDecimal amount, String currency, String username);

    /**
     * Withdraws a specified amount of money from an account in a given currency.
     *
     * @param accountId the ID of the account to withdraw from.
     * @param amount the amount of money to withdraw (must be greater than zero).
     * @param currency the currency of the withdrawal (e.g., "USD", "EUR").
     * @return the {@link Transaction} representing the withdrawal operation.
     */
    Transaction withdraw(Long accountId, BigDecimal amount, String currency);

    /**
     * Retrieves the balances of all currencies associated with a specific account.
     *
     * @param accountId the ID of the account whose balances are being retrieved.
     * @return a map where the keys are currency codes (e.g., "USD", "EUR") and the values are balances.
     */
    Map<String, BigDecimal> getAccountBalances(Long accountId);

    /**
     * Exchanges a specified amount of money from one currency to another within an account.
     *
     * @param accountId the ID of the account performing the currency exchange.
     * @param fromCurrency the currency to convert from (e.g., "USD").
     * @param toCurrency the currency to convert to (e.g., "EUR").
     * @param amount the amount of money to exchange (must be less than or equal to the balance in fromCurrency).
     * @return a list of {@link Transaction} objects representing the withdrawal and deposit operations involved in the exchange.
     */
    List<Transaction> exchangeCurrency(Long accountId, String fromCurrency, String toCurrency, BigDecimal amount);

}
