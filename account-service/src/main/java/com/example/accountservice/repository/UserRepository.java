package com.example.accountservice.repository;

import com.example.accountservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 *
 * This interface provides methods for performing CRUD operations and custom queries
 * on the `users` table in the database. It extends {@link JpaRepository}, which
 * includes standard methods for entity persistence.
 *
 * Key Features:
 * - Retrieves a user by their username.
 * - Leverages Spring Data JPA for simplified database interactions.
 *
 * Methods:
 * - {@link #findByUsername(String)}: Retrieves an {@link Optional} containing a {@link User} entity by username.
 *
 * Parameters:
 * - {@code User}: The type of entity managed by this repository.
 * - {@code Long}: The type of the entity's primary key.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
