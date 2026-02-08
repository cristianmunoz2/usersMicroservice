package com.pragma.usersMicroservice.infrastructure.output.jpa.repository;

import com.pragma.usersMicroservice.domain.util.RoleName;
import com.pragma.usersMicroservice.infrastructure.output.jpa.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing Role data in the database.
 * Extends JpaRepository to provide standard CRUD operations.
 */
public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {

    /**
     * Retrieves a RoleEntity based on its name.
     * <p>
     * Spring Data JPA automatically handles the conversion between the
     * RoleName enum and the corresponding string value in the database.
     * </p>
     *
     * @param name The RoleName enum to search for (e.g., ROLE_ADMIN).
     * @return An Optional containing the RoleEntity if found, or empty if not.
     */
    Optional<RoleEntity> findByName(RoleName name);
}