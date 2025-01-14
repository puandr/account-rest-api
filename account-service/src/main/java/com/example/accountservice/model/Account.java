package com.example.accountservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a bank account in the system.
 *
 * This class maps to the `accounts` table in the database and includes information
 * about the account number, owner, balances, and creation time.
 *
 * Relationships:
 * - One-to-Many with {@link Balance}: Represents the balances held by this account in various currencies.
 * - Many-to-One with {@link User}: Represents the user who owns the account.
 *
 * Annotations:
 * - {@link Entity}: Marks this class as a JPA entity.
 * - {@link Table}: Specifies the database table name (`accounts`).
 * - {@link Data}: Generates boilerplate code such as getters, setters, equals, hashCode, and toString.
 * - {@link NoArgsConstructor}: Generates a no-argument constructor.
 * - {@link AllArgsConstructor}: Generates a constructor with all fields.
 * - {@link ToString.Exclude}: Prevents potential infinite recursion in the `balances` relationship.
 *
 * Fields:
 * - {@code id}: The unique identifier for the account, auto-generated.
 * - {@code accountNumber}: A unique account number for the account.
 * - {@code ownerName}: The name of the account's owner.
 * - {@code balances}: A list of balances associated with this account.
 * - {@code createdAt}: The timestamp when the account was created.
 * - {@code owner}: The user who owns the account.
 */
@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private String ownerName;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Balance> balances;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
}
