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
    String generateToken(User user);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
}
