package com.pragma.usersmicroservice.infrastructure.output.jpa.entity;

import com.pragma.usersmicroservice.domain.util.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Persistence entity representing the 'roles' table in the database.
 * <p>
 * This class maps the {@link RoleName} enum and role details to the
 * physical database schema using JPA annotations.
 * </p>
 */
@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleEntity {

    /**
     * Primary key of the role. Auto-incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique name of the role.
     * Persisted as a String (e.g., "ROLE_ADMIN") for readability.
     */
    @Enumerated(EnumType.STRING)
    private RoleName name;

    /**
     * A brief description of the role's purpose or permissions.
     */
    private String description;

}