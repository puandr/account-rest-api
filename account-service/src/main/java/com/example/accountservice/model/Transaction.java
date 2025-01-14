package com.example.accountservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a financial transaction associated with a bank account.
 *
 * This class maps to the `transactions` table in the database and includes information
 * about the transaction type, currency, amount, timestamp, and the associated account.
 *
 * Relationships:
 * - Many-to-One with {@link Account}: Represents the account to which this transaction is associated.
 *
 * Annotations:
 * - {@link Entity}: Marks this class as a JPA entity.
 * - {@link Table}: Specifies the database table name (`transactions`).
 * - {@link Data}: Generates boilerplate code such as getters, setters, equals, hashCode, and toString.
 * - {@link NoArgsConstructor}: Generates a no-argument constructor.
 * - {@link AllArgsConstructor}: Generates a constructor with all fields.
 *
 * Fields:
 * - {@code id}: The unique identifier for the transaction, auto-generated.
 * - {@code account}: The {@link Account} associated with this transaction.
 * - {@code currency}: The currency in which the transaction was made (e.g., "USD", "EUR").
 * - {@code amount}: The amount involved in the transaction.
 * - {@code transactionType}: The type of transaction (e.g., DEPOSIT, WITHDRAW).
 * - {@code timestamp}: The date and time when the transaction occurred.
 */
@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
