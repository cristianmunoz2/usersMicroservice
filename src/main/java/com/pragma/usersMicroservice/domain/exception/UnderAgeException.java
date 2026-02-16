package com.pragma.usersMicroservice.domain.exception;

/**
 * Exception thrown when a user does not meet the minimum age requirement.
 * <p>
 * This runtime exception indicates a violation of the business rule
 * regarding the legal age for user registration.
 * </p>
 */
public class UnderAgeException extends RuntimeException {

    /**
     * Constructs a new UnderAgeException with a default error message.
     */
    public UnderAgeException() {
        super("El usuario a registrar es menor de edad");
    }
}