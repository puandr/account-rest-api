package com.example.accountservice.service;

import com.example.accountservice.model.Transaction;
import com.example.accountservice.model.TransactionType;

import java.math.BigDecimal;
/**
 * Interface defining the contract for managing transactions within the banking application.
 *
 * This service provides functionality to:
 * - Record transactions such as deposits, withdrawals, and currency exchanges.
 *
 * Implementing classes must ensure that transactions are associated with valid accounts and persisted correctly.
 */
public interface TransactionService {

    /**
     * Records a transaction for a specific account.
     *
     * @param accountId the ID of the account associated with the transaction.
     * @param currency the currency involved in the transaction (e.g., "USD", "EUR").
     * @param amount the amount of money involved in the transaction.
     * @param type the {@link TransactionType} indicating the type of transaction (e.g., DEPOSIT, WITHDRAW, EXCHANGE).
     * @return the recorded {@link Transaction} entity containing the transaction details.
     * @throws IllegalArgumentException if any input parameters are invalid (e.g., null or negative values).
     * @throws RuntimeException if an error occurs while recording the transaction (specific exceptions may be defined in implementations).
     */
    Transaction recordTransaction(Long accountId, String currency, BigDecimal amount, TransactionType type);
}
