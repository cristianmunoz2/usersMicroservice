package com.pragma.usersMicroservice.domain.spi;

import com.pragma.usersMicroservice.domain.model.User;

import java.util.Optional;

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

    /**
     * Finds a User by email.
     * @param email The email to search for.
     * @return An Optional containing the User if found, or empty if not found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Validates if a user with the given ID exists in the database.
     * @param id The ID of the user to check for existence.
     * @return True if a user with the given ID exists, false otherwise.
     */
    boolean existsById(String id);

    /**
     * Gets the phone number of a user by their email.
     * @param email The email of the user.
     * @return The phone number of the user.
     */
    String getPhoneByEmail(String email);
}
