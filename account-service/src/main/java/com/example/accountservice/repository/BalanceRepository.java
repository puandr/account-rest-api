package com.example.accountservice.repository;

import com.example.accountservice.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link Balance} entities.
 *
 * This interface provides methods for performing CRUD operations and custom queries
 * on the `balances` table in the database. It extends {@link JpaRepository}, which
 * includes standard methods for entity persistence.
 *
 * Key Features:
 * - Retrieves all balances associated with a specific account.
 * - Finds a balance for a specific account and currency.
 * - Leverages Spring Data JPA for streamlined database interactions.
 *
 * Annotations:
 * - {@link Repository}: Marks this interface as a Spring-managed repository component.
 *
 * Methods:
 * - {@link #findByAccountId(Long)}: Retrieves a list of {@link Balance} entities associated with a specific account ID.
 * - {@link #findByAccountIdAndCurrency(Long, String)}: Retrieves a specific {@link Balance} entity for an account and currency.
 *
 * Parameters:
 * - {@code Balance}: The type of entity managed by this repository.
 * - {@code Long}: The type of the entity's primary key.
 */
@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    List<Balance> findByAccountId(Long accountId);

    Balance findByAccountIdAndCurrency(Long accountId, String currency);
}
