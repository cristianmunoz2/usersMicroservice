package com.pragma.usersmicroservice.domain.exception;

/**
 * Exception thrown when attempting to register a user with an identity document that is already in use.
 * <p>
 * This exception enforces the business rule that each user must have a unique
 * identification document within the system.
 * </p>
 */
public class IdDocAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new instance with a default message indicating the conflict.
     */
    public IdDocAlreadyExistsException() {
        super("Id Document is already registered in BD");
    }
}