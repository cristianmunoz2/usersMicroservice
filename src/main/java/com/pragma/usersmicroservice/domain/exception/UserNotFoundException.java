package com.pragma.usersmicroservice.domain.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found with the provided email");
    }
}
