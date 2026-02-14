package com.pragma.usersMicroservice.domain.api;

import com.pragma.usersMicroservice.application.dto.LoginRequest;
import com.pragma.usersMicroservice.application.dto.UserRegisterRequest;
import com.pragma.usersMicroservice.domain.model.User;

/**
 * Represents a User port.
 * This connects model with the application layer through use cases.
 */
public interface IUserServicePort {
    /**
     * Creates a User with the Owner role.
     *
     * @param user {@link User} to be registered as Owner.
     * @return The created {@link User}.
     */
    User createOwner(User user);

    /**
     * Registers a new user with the provided registration details.
     *
     * @param request The user registration request containing necessary details.
     * @return A login request to authenticate user later.
     */
    LoginRequest registerCustomer(UserRegisterRequest request);
}
