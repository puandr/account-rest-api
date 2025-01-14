package com.example.accountservice.repository;

import com.example.accountservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Account} entities.
 *
 * This interface provides methods for performing CRUD operations and custom queries
 * on the `accounts` table in the database. It extends {@link JpaRepository}, which
 * includes standard methods for entity persistence.
 *
 * Key Features:
 * - Provides a custom method to find an account by its account number.
 * - Leverages Spring Data JPA to simplify database interactions.
 *
 * Annotations:
 * - {@link Repository}: Marks this interface as a Spring-managed repository component.
 *
 * Methods:
 * - {@link #findByAccountNumber(String)}: Retrieves an {@link Account} entity based on the provided account number.
 *
 * Parameters:
 * - {@code Account}: The type of entity managed by this repository.
 * - {@code Long}: The type of the entity's primary key.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String accountNumber);
}
