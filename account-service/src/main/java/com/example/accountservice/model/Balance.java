package com.example.accountservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Entity representing a balance in a specific currency for a bank account.
 *
 * This class maps to the `balances` table in the database and includes information about
 * the currency and the associated balance amount for a specific account.
 *
 * Relationships:
 * - Many-to-One with {@link Account}: Represents the account to which this balance belongs.
 *
 * Annotations:
 * - {@link Entity}: Marks this class as a JPA entity.
 * - {@link Table}: Specifies the database table name (`balances`).
 * - {@link Data}: Generates boilerplate code such as getters, setters, equals, hashCode, and toString.
 * - {@link NoArgsConstructor}: Generates a no-argument constructor.
 * - {@link AllArgsConstructor}: Generates a constructor with all fields.
 * - {@link ToString.Exclude}: Prevents potential infinite recursion in the `account` relationship.
 *
 * Fields:
 * - {@code id}: The unique identifier for the balance, auto-generated.
 * - {@code account}: The {@link Account} associated with this balance.
 * - {@code currency}: The currency code of this balance (e.g., "USD", "EUR").
 * - {@code amount}: The amount held in the specified currency.
 */
@Entity
@Table(name = "balances")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @ToString.Exclude
    private Account account;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private BigDecimal amount;
}
