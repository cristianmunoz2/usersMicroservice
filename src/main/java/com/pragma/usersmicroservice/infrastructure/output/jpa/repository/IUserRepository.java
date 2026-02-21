package com.pragma.usersmicroservice.infrastructure.output.jpa.repository;

import com.pragma.usersmicroservice.infrastructure.output.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repository interface for User data access.
 * <p>
 * Extends {@link JpaRepository} to provide standard CRUD operations and database interactions
 * for the {@link UserEntity} without writing boilerplate code.
 * </p>
 */
public interface IUserRepository extends JpaRepository<UserEntity, String> {
    /**
     * Validates if an email is already registered in BD.
     * @param email email to validate
     * @return True if the email exists. False if not.
     */
    boolean existsByEmail(String email);

    /**
     * Validates if an ID Document is already registered in BD.
     * @param idDocument Id document number to validate
     * @return True if the ID Document number exists. False if not.
     */
    boolean existsByIdDocument(String idDocument);

    /**
     * Finds a UserEntity by email.
     * @param email The email to search for.
     * @return An Optional containing the UserEntity if found, or empty if not found.
     */
    Optional<UserEntity> findByEmail(String email);


    /**
     * Validates if a user with the given ID exists in BD.
     * @param id The user ID to validate
     * @return True if the user ID exists. False if not.
     */
    boolean existsById(String id);


    @Query("SELECT u.email FROM UserEntity u WHERE u.id = :id")
    String findEmailById(String id);
}