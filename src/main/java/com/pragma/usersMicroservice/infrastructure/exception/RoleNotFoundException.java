package com.pragma.usersMicroservice.infrastructure.exception;

/**
 * Exception thrown when a specific Role entity cannot be found in the database.
 * <p>
 * This indicates a data consistency issue, such as trying to assign a role
 * that has not been created or seeded in the persistence layer.
 * </p>
 */
public class RoleNotFoundException extends RuntimeException {

    /**
     * Constructs a new instance with a default message indicating the missing resource.
     */
    public RoleNotFoundException() {
        super("Role not found in DB.");
    }
}