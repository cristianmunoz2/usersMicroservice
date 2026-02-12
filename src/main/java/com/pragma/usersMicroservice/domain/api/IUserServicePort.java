package com.pragma.usersMicroservice.domain.api;

import com.pragma.usersMicroservice.domain.model.User;

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
}
