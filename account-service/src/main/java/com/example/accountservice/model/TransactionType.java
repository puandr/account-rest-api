package com.example.accountservice.model;

/**
 * Enum representing the types of financial transactions that can be performed.
 *
 * This enum is used to distinguish between different transaction operations,
 * such as deposits and withdrawals, and is typically associated with the {@link Transaction} entity.
 *
 * Values:
 * - {@code DEPOSIT}: Represents a deposit transaction, where money is added to an account.
 * - {@code WITHDRAW}: Represents a withdrawal transaction, where money is removed from an account.
 */
public enum TransactionType {
    DEPOSIT,
    WITHDRAW
}
