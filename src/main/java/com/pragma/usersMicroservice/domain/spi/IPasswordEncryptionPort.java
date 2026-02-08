package com.pragma.usersMicroservice.domain.spi;

public interface IPasswordEncryptionPort {
    String encode(String password);
    boolean matches(String rawPassword, String encodedPassword);
}
