package com.pragma.usersmicroservice.domain.exception;

/**
 * Exception thrown when attempting to register a user with an email address that is already in use.
 * <p>
 * This exception enforces the unique email business rule, preventing duplicate accounts
 * within the system.
 * </p>
 */
public class EmailAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new instance with a default message indicating the conflict.
     */
    public EmailAlreadyExistsException() {
        super("Email already exists in BD");
    }
}