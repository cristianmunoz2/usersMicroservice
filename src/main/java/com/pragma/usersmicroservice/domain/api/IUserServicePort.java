package com.pragma.usersmicroservice.domain.api;

import com.pragma.usersmicroservice.domain.model.User;

/**
 * Represents a User port.
 * This connects model with the application layer through use cases.
 */
public interface IUserServicePort {
    /**
     * Creates a User with the Owner role.
     * @param user {@link User} to be registered as Owner.
     * @return The created {@link User}.
     */
    User createOwner(User user);

    /**
     * Creates a User with the Employee role.
     * @param user {@link User} to be registered as Employee.
     * @return The created {@link User}.
     */
    void createEmployee(User user);

    void createCustomer(User user);

    boolean existsById(String id);

    /**
     * Returns the phone number of a user by their ID.
     * @param id The ID of the user.
     * @return The phone number of the user.
     */
    String getPhoneById(String id);

    String findEmailById(String id);
}
