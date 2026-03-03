package com.pragma.usersmicroservice.domain.spi;

/**
 * Port for password encryption operations.
 * <p>
 * Defines the contract for encoding passwords and verifying matches, allowing
 * for different encryption implementations to be used in the application.
 * </p>
 */
public interface IPasswordEncryptionPort {
    /**
     * Encodes a raw password using the chosen encryption algorithm.
     * @param password The raw password to encode.
     * @return The encoded password.
     */
    String encode(String password);
    /**
     * Verifies if a raw password matches the encoded password.
     * @param rawPassword The raw password to check.
     * @param encodedPassword The encoded password to compare against.
     * @return True if the passwords match, false otherwise.
     */
    boolean matches(String rawPassword, String encodedPassword);
}
