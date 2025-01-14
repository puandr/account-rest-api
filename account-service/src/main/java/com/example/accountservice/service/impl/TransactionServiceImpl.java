package com.example.accountservice.service.impl;

import com.example.accountservice.model.Account;
import com.example.accountservice.model.Transaction;
import com.example.accountservice.model.TransactionType;
import com.example.accountservice.repository.AccountRepository;
import com.example.accountservice.repository.TransactionRepository;
import com.example.accountservice.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Implementation of the {@link TransactionService} interface for managing transaction-related operations.
 *
 * This service provides functionality to:
 * - Record transactions (e.g., deposits, withdrawals, and currency exchanges).
 *
 * Responsibilities:
 * - Ensure that transactions are associated with a valid account.
 * - Persist transaction data into the database.
 *
 * Dependencies:
 * - {@link TransactionRepository}: For persisting transaction data.
 * - {@link AccountRepository}: For validating the existence of accounts.
 *
 * Exceptions:
 * - {@link EntityNotFoundException}: Thrown when an account with the given ID is not found.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Records a transaction for a specific account, including deposits, withdrawals, or other transaction types.
     *
     * @param accountId the ID of the account associated with the transaction.
     * @param currency the currency involved in the transaction (e.g., "USD", "EUR").
     * @param amount the amount of money involved in the transaction.
     * @param type the {@link TransactionType} indicating the type of transaction (e.g., DEPOSIT, WITHDRAW).
     * @return the saved {@link Transaction} entity.
     * @throws EntityNotFoundException if the account with the given ID does not exist.
     */
    @Override
    @Transactional
    public Transaction recordTransaction(Long accountId, String currency, BigDecimal amount, TransactionType type) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));

        Transaction transaction = new Transaction(null, account, currency, amount, type, LocalDateTime.now());

        return transactionRepository.save(transaction);
    }
}
