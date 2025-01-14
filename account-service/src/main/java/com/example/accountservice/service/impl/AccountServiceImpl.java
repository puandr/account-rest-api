package com.example.accountservice.service.impl;

import com.example.accountservice.dto.CurrencyConversionResponseDto;
import com.example.accountservice.exception.AccessDeniedException;
import com.example.accountservice.exception.InsufficientFundsException;
import com.example.accountservice.model.Account;
import com.example.accountservice.model.Balance;
import com.example.accountservice.model.Transaction;
import com.example.accountservice.model.TransactionType;
import com.example.accountservice.repository.AccountRepository;
import com.example.accountservice.repository.BalanceRepository;
import com.example.accountservice.service.AccountService;
import com.example.accountservice.service.CurrencyExchangeService;
import com.example.accountservice.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link AccountService} interface for managing account-related operations.
 *
 * This service provides functionality for:
 * - Depositing funds into an account.
 * - Withdrawing funds from an account.
 * - Retrieving account balances.
 * - Exchanging currencies within an account.
 *
 * Dependencies:
 * - {@link AccountRepository}: To manage account persistence.
 * - {@link BalanceRepository}: To manage balance persistence.
 * - {@link TransactionService}: To record and manage transactions.
 * - {@link CurrencyExchangeService}: To handle currency conversion operations.
 *
 * All methods are transactional and ensure atomicity of operations.
 */
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);


    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;
    private final TransactionService transactionService;
    private final CurrencyExchangeService currencyExchangeService;

    public AccountServiceImpl(AccountRepository accountRepository,
                              BalanceRepository balanceRepository,
                              TransactionService transactionService,
                              CurrencyExchangeService currencyExchangeService) {
        this.accountRepository = accountRepository;
        this.balanceRepository = balanceRepository;
        this.transactionService = transactionService;
        this.currencyExchangeService = currencyExchangeService;
    }


    /**
     * Deposits a specified amount of money into an account in a given currency.
     *
     * @param accountId the ID of the account to deposit into.
     * @param amount the amount of money to deposit (must be greater than zero).
     * @param currency the currency of the deposit.
     * @param username the username of the person performing the deposit (for authorization checks).
     * @return the recorded {@link Transaction} for the deposit operation.
     * @throws IllegalArgumentException if the deposit amount is less than or equal to zero.
     * @throws EntityNotFoundException if the account does not exist.
     * @throws AccessDeniedException if the username does not match the account owner.
     */
    @Override
    @Transactional
    public Transaction deposit(Long accountId, BigDecimal amount, String currency, String username) {
        logger.info("Starting deposit: username={}, accountId={}, amount={}, currency={}", username, accountId, amount, currency);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Invalid deposit amount: {}", amount);
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));

        if (!account.getOwner().getUsername().equals(username)) {
            logger.error("Unauthorized deposit attempt: username={}, accountId={}", username, accountId);
            throw new AccessDeniedException("You do not have permission to deposit into this account");
        }

        Balance balance = balanceRepository.findByAccountIdAndCurrency(accountId, currency);
        if (balance == null) {
            logger.info("Creating new balance entry: accountId={}, currency={}", accountId, currency);
            balance = new Balance(null, account, currency, BigDecimal.ZERO);
        }

        balance.setAmount(balance.getAmount().add(amount));
        balanceRepository.save(balance);
        logger.info("Deposit completed: accountId={}, new balance={}", accountId, balance.getAmount());

        Transaction transaction = transactionService.recordTransaction(accountId, currency, amount, TransactionType.DEPOSIT);
        logger.info("Transaction recorded: accountId={}, amount={}, currency={}, type={}", accountId, amount, currency, TransactionType.DEPOSIT);

        return transaction;
    }

    /**
     * Withdraws a specified amount of money from an account in a given currency.
     *
     * @param accountId the ID of the account to withdraw from.
     * @param amount the amount of money to withdraw (must be greater than zero).
     * @param currency the currency of the withdrawal.
     * @return the recorded {@link Transaction} for the withdrawal operation.
     * @throws IllegalArgumentException if the withdrawal amount is less than or equal to zero.
     * @throws EntityNotFoundException if the account does not exist.
     * @throws InsufficientFundsException if the account does not have enough funds in the specified currency.
     */
    @Override
    @Transactional
    public Transaction withdraw(Long accountId, BigDecimal amount, String currency) {
        logger.info("Starting withdrawal: accountId={}, amount={}, currency={}", accountId, amount, currency);


        if (!accountRepository.existsById(accountId)) {
            String errorMessage = "Account not found with ID: " + accountId;
            logger.error(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Invalid withdrawal amount: {}", amount);
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }

        Balance balance = balanceRepository.findByAccountIdAndCurrency(accountId, currency);
        if (balance == null || balance.getAmount().compareTo(amount) < 0) {
            logger.error("Insufficient funds: accountId={}, amount={}, balance={}", accountId, amount, balance != null ? balance.getAmount() : "null");
            throw new InsufficientFundsException(String.format(
                    "Insufficient funds for accountId=%d. Attempted withdrawal: %s %s. Available balance: %s %s",
                    accountId, amount, currency, balance != null ? balance.getAmount() : "0.00", currency
            ));
        }

        balance.setAmount(balance.getAmount().subtract(amount));
        balanceRepository.save(balance);
        logger.info("Withdrawal completed: accountId={}, new balance={}", accountId, balance.getAmount());

        Transaction transaction = transactionService.recordTransaction(accountId, currency, amount, TransactionType.WITHDRAW);
        logger.info("Transaction recorded: accountId={}, amount={}, currency={}, type={}", accountId, amount, currency, TransactionType.WITHDRAW);

        return transaction;
    }

    /**
     * Retrieves the balances of all currencies for a specific account.
     *
     * @param accountId the ID of the account whose balances are being retrieved.
     * @return a map where the keys are currency codes, and the values are balances.
     * @throws EntityNotFoundException if the account does not exist.
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getAccountBalances(Long accountId) {
        logger.info("Fetching balances for accountId={}", accountId);

        accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    logger.error("Account not found for balance retrieval: accountId={}", accountId);
                    return new EntityNotFoundException("Account not found with ID: " + accountId);
                });

        List<Balance> balances = balanceRepository.findByAccountId(accountId);
        logger.info("Balances retrieved for accountId={}: {}", accountId, balances);

        return balances.stream()
                .collect(Collectors.toMap(Balance::getCurrency, Balance::getAmount));
    }

    /**
     * Exchanges a specified amount of money from one currency to another within an account.
     *
     * @param accountId the ID of the account where the exchange is performed.
     * @param fromCurrency the currency being converted from.
     * @param toCurrency the currency being converted to.
     * @param amount the amount of money to convert (must be less than or equal to the balance in fromCurrency).
     * @return a list of {@link Transaction} objects representing the withdrawal and deposit operations.
     * @throws EntityNotFoundException if the account does not exist.
     * @throws InsufficientFundsException if the account does not have enough funds in the fromCurrency.
     */
    @Override
    @Transactional
    public List<Transaction> exchangeCurrency(Long accountId, String fromCurrency, String toCurrency, BigDecimal amount) {
        logger.info("Starting currency exchange: accountId={}, fromCurrency={}, toCurrency={}, amount={}", accountId, fromCurrency, toCurrency, amount);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));

        Balance fromBalance = balanceRepository.findByAccountIdAndCurrency(accountId, fromCurrency);
        if (fromBalance == null || fromBalance.getAmount().compareTo(amount) < 0) {
            throw new InsufficientFundsException(String.format("Insufficient funds in %s for accountId=%d", fromCurrency, accountId));
        }

        CurrencyConversionResponseDto conversionResponse = currencyExchangeService.convert(fromCurrency, toCurrency, amount);

        fromBalance.setAmount(fromBalance.getAmount().subtract(amount));
        balanceRepository.save(fromBalance);

        Balance toBalance = balanceRepository.findByAccountIdAndCurrency(accountId, toCurrency);

        if (toBalance == null) {
            logger.info("Creating a new balance for accountId={} and currency={}", accountId, toCurrency);
            toBalance = new Balance(null, account, toCurrency, BigDecimal.ZERO);
        } else {
            logger.info("Updating existing balance for accountId={} and currency={}", accountId, toCurrency);
        }
        toBalance.setAmount(toBalance.getAmount().add(conversionResponse.getConvertedAmount()));
        balanceRepository.save(toBalance);


        Transaction transactionWithdraw = transactionService.recordTransaction(accountId, fromCurrency, amount, TransactionType.WITHDRAW);
        Transaction transactionDeposit = transactionService.recordTransaction(accountId, toCurrency, conversionResponse.getConvertedAmount(), TransactionType.DEPOSIT);

        logger.info("Currency exchange completed: accountId={}, fromCurrency={}, toCurrency={}, convertedAmount={}",
                accountId, fromCurrency, toCurrency, conversionResponse.getConvertedAmount());

        return List.of(transactionWithdraw, transactionDeposit);
    }

}
