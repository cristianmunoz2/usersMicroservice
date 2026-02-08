package com.pragma.usersMicroservice.domain.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Email already exists in BD");
    }
}
