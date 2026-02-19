package com.pragma.usersmicroservice.domain.spi;

import com.pragma.usersmicroservice.domain.model.Role;
import com.pragma.usersmicroservice.domain.util.RoleName;

import java.util.Optional;

/**
 * Connects model with the application layers to implement Role use cases.
 * It must connect with a secondary adapter.
 */
public interface IRolePersistencePort {
    /**
     * Find a role by its name.
     * @param name
     * @return The saved Role.
     */
    Optional<Role> findByName(RoleName name);

    /**
     * Find a role by its unique identifier.
     * @param id The unique identifier of the role (as a String).
     * @return An Optional containing the Role if found, or empty if not.
     */
    Optional<Role> findById(Long id);
}
