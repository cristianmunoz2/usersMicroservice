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

}