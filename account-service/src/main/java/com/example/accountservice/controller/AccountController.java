package com.example.accountservice.controller;

import com.example.accountservice.dto.CurrencyExchangeRequestDto;
import com.example.accountservice.dto.DepositRequestDto;
import com.example.accountservice.dto.WithdrawRequestDto;
import com.example.accountservice.dto.BalanceResponseDto;
import com.example.accountservice.model.Transaction;
import com.example.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing account-related operations.
 *
 * This controller provides endpoints for:
 * - Depositing money into an account.
 * - Withdrawing money from an account.
 * - Fetching account balances.
 * - Performing currency exchanges.
 *
 * Each endpoint validates user inputs, ensures proper authorization, and logs transactions for auditing purposes.
 * The controller leverages Spring's validation framework and integrates with the {@link AccountService}.
 *
 * Annotations:
 * - {@link RestController}: Marks this class as a REST controller for Spring MVC.
 * - {@link RequestMapping}: Defines the base path `/accounts` for all endpoints in this controller.
 * - {@link Tag}: Provides metadata for Swagger/OpenAPI documentation.
 */
@RestController
@RequestMapping("/accounts")
@Tag(name = "Account Service", description = "API for managing account transactions, get balances, and currency exchange")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Deposits money into an account.
     *
     * Validates the user's authorization and the input parameters before updating the account balance.
     * Logs the transaction for auditing purposes and returns the transaction details.
     *
     * @param accountId the ID of the account to deposit into.
     * @param request the {@link DepositRequestDto} containing the deposit details (amount and currency).
     * @param principal the {@link Principal} containing the authenticated user's information.
     * @return a {@link ResponseEntity} containing a success message and transaction details.
     */
    @Operation(summary = "Deposit money into an account",
            description = "Allows users to deposit a specific amount of money into their account in a specified currency. "
            + "This operation validates the input to ensure that the account exists, the user is authorized to make deposits, "
            + "and the amount is within acceptable limits. Additionally, the system logs the transaction for auditing purposes.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Money deposited successfully. The account balance is updated and a transaction record is created.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request payload, such as a negative deposit amount or unsupported currency.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access. The user is not authenticated or does not have permissions for this action.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. The user is attempting to deposit money into an account they do not own or manage.",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<Map<String, Object>> deposit(
            @PathVariable("accountId") Long accountId,
            @Valid @RequestBody DepositRequestDto request,
            Principal principal) {
        String username = principal.getName();
        logger.info("Deposit request: username={}, accountId={}, amount={}, currency={}", username, accountId, request.getAmount(), request.getCurrency());

        Transaction transaction = accountService.deposit(accountId, request.getAmount(), request.getCurrency(), username);

        Map<String, Object> response = Map.of(
                "status", "success",
                "message", "Deposit successful",
                "transaction", Map.of(
                        "transactionId", transaction.getId(),
                        "accountId", transaction.getAccount().getId(),
                        "amount", transaction.getAmount(),
                        "currency", transaction.getCurrency(),
                        "timestamp", transaction.getTimestamp(),
                        "transactionType", transaction.getTransactionType().name()
                )
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Withdraws money from an account.
     *
     * Ensures that the account has sufficient balance and that the user is authorized to perform the operation.
     * Logs the transaction for auditing purposes and returns the transaction details.
     *
     * @param accountId the ID of the account to withdraw from.
     * @param request the {@link WithdrawRequestDto} containing the withdrawal details (amount and currency).
     * @return a {@link ResponseEntity} containing a success message and transaction details.
     */
    @Operation(summary = "Withdraw money from an account",
            description = "Facilitates the withdrawal of a specified amount of money from an account in a specified currency. "
            + "This endpoint ensures that the account has sufficient balance in the given currency and that the user "
            + "has the necessary permissions to perform the withdrawal. Any violations result in an appropriate error response." )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Withdrawal successful. The account balance is updated and the transaction is logged.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request payload, such as attempting to withdraw a negative amount or an unsupported currency.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. The user is attempting to withdraw money from an account they do not manage.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Insufficient funds. The account does not have enough balance in the specified currency.",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<Map<String, Object>> withdraw(
            @PathVariable("accountId") Long accountId,
            @Valid @RequestBody WithdrawRequestDto request) {
        logger.info("Withdraw request: accountId={}, amount={}, currency={}", accountId, request.getAmount(), request.getCurrency());

        Transaction transaction = accountService.withdraw(accountId, request.getAmount(), request.getCurrency());

        logger.info("Withdrawal successful for accountId={}, amount={}, currency={}", accountId, request.getAmount(), request.getCurrency());

        Map<String, Object> response = Map.of(
                "status", "success",
                "message", "Withdrawal successful",
                "transaction", Map.of(
                        "transactionId", transaction.getId(),
                        "accountId", transaction.getAccount().getId(),
                        "amount", transaction.getAmount(),
                        "currency", transaction.getCurrency(),
                        "timestamp", transaction.getTimestamp(),
                        "transactionType", transaction.getTransactionType().name()
                )
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the balances of all currencies associated with an account.
     *
     * Validates the account's existence and the user's authorization before returning the balances.
     * Each currency and its corresponding balance are included in the response.
     *
     * @param accountId the ID of the account to retrieve balances for.
     * @return a {@link ResponseEntity} containing a list of {@link BalanceResponseDto} objects.
     */
    @Operation(summary = "Retrieve Account Balances",
            description = "Fetches the balances of all currencies associated with the specified account. "
                    + "Each currency is returned with its corresponding balance, allowing users to view "
                    + "their holdings in a multi-currency account. The request validates that the account exists "
                    + "and that the user has proper authorization to access this information.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account balances retrieved successfully. A list of all currencies with their respective balances is returned.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BalanceResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access. The user is not authenticated or does not have permission to view this account.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found. The specified account ID does not exist in the system.",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{accountId}/balances")
    public ResponseEntity<List<BalanceResponseDto>> getBalances(@PathVariable("accountId") Long accountId) {
        logger.info("Get balances request for accountId={}", accountId);

        Map<String, BigDecimal> balances = accountService.getAccountBalances(accountId);
        List<BalanceResponseDto> response = balances.entrySet().stream()
                .map(entry -> new BalanceResponseDto(entry.getKey(), entry.getValue()))
                .toList();

        logger.info("Balances retrieved for accountId={}: {}", accountId, response);

        return ResponseEntity.ok(response);
    }

    /**
     * Performs a currency exchange operation for a specific account.
     *
     * Validates the user's authorization, account existence, and sufficient balance in the source currency.
     * Logs the transactions for auditing purposes and updates the account balances accordingly.
     *
     * @param accountId the ID of the account performing the exchange.
     * @param request the {@link CurrencyExchangeRequestDto} containing the exchange details (fromCurrency, toCurrency, amount).
     * @param principal the {@link Principal} containing the authenticated user's information.
     * @return a {@link ResponseEntity} containing a success message and the details of the transactions.
     */
    @Operation(
            summary = "Exchange Currency Balances",
            description = "Performs a currency exchange operation for the specified account. "
                    + "This endpoint allows the user to convert an amount from one currency to another, provided both currencies "
                    + "are supported by the system. The service ensures that the account has sufficient balance in the source currency, "
                    + "and a fixed exchange rate is used for the conversion. The transaction is logged for auditing purposes.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Currency exchange completed successfully. The account balances are updated accordingly.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request payload. This may include unsupported currencies, invalid amounts, or malformed requests.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access. The user is not authenticated or does not have permissions for this operation.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error. An issue occurred during the currency exchange operation.",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/{accountId}/exchange")
    public ResponseEntity<Map<String, Object>> exchangeCurrency(
            @PathVariable("accountId") Long accountId,
            @Valid @RequestBody CurrencyExchangeRequestDto request,
            Principal principal) {
        String username = principal.getName();

        logger.info("Currency exchange request: username={}, accountId={}, fromCurrency={}, toCurrency={}, amount={}",
                username, accountId, request.getFromCurrency(), request.getToCurrency(), request.getAmount());

        List<Transaction> transactions = accountService.exchangeCurrency(accountId, request.getFromCurrency(), request.getToCurrency(), request.getAmount());

        Map<String, Object> response = Map.of(
                "status", "success",
                "message", "Currency exchange completed successfully",
                "transactions", transactions.stream().map(transaction -> Map.of(
                        "transactionId", transaction.getId(),
                        "accountId", transaction.getAccount().getId(),
                        "amount", transaction.getAmount(),
                        "currency", transaction.getCurrency(),
                        "transactionType", transaction.getTransactionType().name(),
                        "timestamp", transaction.getTimestamp()
                )).toList()
        );

        return ResponseEntity.ok(response);
    }
}
