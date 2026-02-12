package com.pragma.usersMicroservice.domain.spi;

import com.pragma.usersMicroservice.domain.model.User;

/**
 * Port for JWT token operations.
 * <p>
 * Defines the contract for generating, parsing, and validating JWT tokens,
 * allowing for different JWT implementations to be used in the application.
 * </p>
 */
public interface IJwtProviderPort {
    /**
     * Generates a JWT token for the given user.
     *
     * @param user The user for whom the token is to be generated.
     * @return A JWT token as a String.
     */
    String generateToken(User user);
    /**
     * Extracts the email from a given JWT token.
     *
     * @param token The JWT token from which to extract the email.
     * @return The email contained in the token.
     */
    String getEmailFromToken(String token);
    /**
     * Validates the given JWT token.
     *
     * @param token The JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    boolean validateToken(String token);

    /**
     * Extracts the role from a given JWT token.
     *
     * @param token The JWT token from which to extract the role.
     * @return The role contained in the token.
     */
    String getRoleFromToken(String token);
}
