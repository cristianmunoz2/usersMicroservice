package com.pragma.usersMicroservice.domain.spi;

import com.pragma.usersMicroservice.domain.model.Role;
import com.pragma.usersMicroservice.domain.util.RoleName;

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
}
