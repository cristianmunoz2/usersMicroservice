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

    /**
     * Validates if a email is already registered in BD
     * @param email
     * @return True if email already exists in BD. False if not.
     */
    boolean existsByEmail(String email);

    /**
     * Validates if an ID Document is already registered in BD.
     * @param idDocument Id document number to validate
     * @return True if the ID Document number exists. False if not.
     */
    boolean existsByIdDocument(String idDocument);
}
