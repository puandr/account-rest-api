package com.example.accountservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity representing a user in the system.
 *
 * This class maps to the `users` table in the database and includes information about
 * the user's credentials and associated accounts.
 *
 * Relationships:
 * - One-to-Many with {@link Account}: Represents the accounts owned by the user.
 *
 * Annotations:
 * - {@link Entity}: Marks this class as a JPA entity.
 * - {@link Table}: Specifies the database table name (`users`).
 * - {@link Data}: Generates boilerplate code such as getters, setters, equals, hashCode, and toString.
 * - {@link NoArgsConstructor}: Generates a no-argument constructor.
 *
 * Fields:
 * - {@code id}: The unique identifier for the user, auto-generated.
 * - {@code username}: The unique username of the user.
 * - {@code password}: The hashed password for the user's authentication.
 * - {@code accounts}: A list of {@link Account} entities owned by the user.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts;
}
