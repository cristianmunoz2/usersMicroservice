package com.pragma.usersmicroservice.application.handler;

import com.pragma.usersmicroservice.application.dto.response.JwtResponse;
import com.pragma.usersmicroservice.application.dto.request.LoginRequest;
import com.pragma.usersmicroservice.application.dto.response.UserValidationResponse;

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
    UserValidationResponse validateToken(String token);
}
