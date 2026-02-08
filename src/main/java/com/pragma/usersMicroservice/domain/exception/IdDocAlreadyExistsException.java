package com.pragma.usersMicroservice.domain.exception;

public class IdDocAlreadyExistsException extends RuntimeException {
    public IdDocAlreadyExistsException() {
        super("Id Document is already registered in BD");
    }
}
