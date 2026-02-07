package com.pragma.usersMicroservice.domain.spi;

import com.pragma.usersMicroservice.domain.model.User;

/**
 * Connects model with the application layers to implement User use cases.
 * It must connect with a secondary adapter.
 */
public interface IUserPersistencePort {
    /**
     * Saves a User in relational DB.
     * @param user
     * @return The saved User.
     */
    User saveUser(User user);
}
