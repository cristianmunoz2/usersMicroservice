package com.pragma.usersMicroservice.domain.exception;

public class UnderAgeException extends RuntimeException {
    public UnderAgeException() {
        super("El usuario a registrar es menor de edad");
    }
}
