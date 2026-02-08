package com.pragma.usersMicroservice.infrastructure.output.jpa.repository;

import com.pragma.usersMicroservice.infrastructure.output.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for User data access.
 * <p>
 * Extends {@link JpaRepository} to provide standard CRUD operations and database interactions
 * for the {@link UserEntity} without writing boilerplate code.
 * </p>
 */
public interface IUserRepository extends JpaRepository<UserEntity, Long> {
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
}