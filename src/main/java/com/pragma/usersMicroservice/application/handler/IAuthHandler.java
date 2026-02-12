package com.pragma.usersMicroservice.application.handler;

import com.pragma.usersMicroservice.application.dto.JwtResponse;
import com.pragma.usersMicroservice.application.dto.LoginRequest;

/**
 * Interface for handling authentication-related operations.
 * <p>
 * This interface defines the contract for authentication processes, such as user login.
 * Implementations of this interface will handle the orchestration of authentication logic,
 * including validating credentials and generating JWT tokens.
 * </p>
 */
public interface IAuthHandler {
    JwtResponse login(LoginRequest loginRequestDto);
}
