package com.pragma.usersMicroservice.domain.api;

import com.pragma.usersMicroservice.application.dto.UserValidationResponse;

/**
 * Port for authentication operations.
 * <p>
 * Defines the contract for user authentication, allowing for different
 * authentication implementations to be used in the application.
 * </p>
 */
public interface    IAuthServicePort {
    /**
     * Authenticates a user with the given email and password.
     *
     * @param email    The email of the user to authenticate.
     * @param password The password of the user to authenticate.
     * @return A JWT token if authentication is successful, or an error message if it fails.
     */
    String login(String email, String password);
    /**
     * Validates the credentials and generates a new JWT token if valid.
     *
     * @param token The JWT token to validate.
     * @return A new JWT token if validation is successful, or an error message if it fails.
     */
    UserValidationResponse validateNewToken(String token);



}
