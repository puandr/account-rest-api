package com.example.accountservice.repository;

import com.example.accountservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link Transaction} entities.
 *
 * This interface provides methods for performing CRUD operations and custom queries
 * on the `transactions` table in the database. It extends {@link JpaRepository}, which
 * includes standard methods for entity persistence.
 *
 * Key Features:
 * - Retrieves all transactions associated with a specific account.
 * - Retrieves transactions for a specific account and currency.
 * - Leverages Spring Data JPA to simplify database interactions.
 *
 * Annotations:
 * - {@link Repository}: Marks this interface as a Spring-managed repository component.
 *
 * Methods:
 * - {@link #findByAccountId(Long)}: Retrieves a list of {@link Transaction} entities for a given account ID.
 * - {@link #findByAccountIdAndCurrency(Long, String)}: Retrieves a list of {@link Transaction} entities for a specific account and currency.
 *
 * Parameters:
 * - {@code Transaction}: The type of entity managed by this repository.
 * - {@code Long}: The type of the entity's primary key.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findByAccountIdAndCurrency(Long accountId, String currency);
}
