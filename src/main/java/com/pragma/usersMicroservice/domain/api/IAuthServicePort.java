package com.pragma.usersMicroservice.domain.api;

/**
 * Port for authentication operations.
 * <p>
 * Defines the contract for user authentication, allowing for different
 * authentication implementations to be used in the application.
 * </p>
 */
public interface IAuthServicePort {
    String login(String email, String password);
}
