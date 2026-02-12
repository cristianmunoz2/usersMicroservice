package com.pragma.usersMicroservice.domain.usecase;

import com.pragma.usersMicroservice.application.dto.UserValidationResponse;
import com.pragma.usersMicroservice.domain.api.IAuthServicePort;
import com.pragma.usersMicroservice.domain.exception.InvalidCredentialsException;
import com.pragma.usersMicroservice.domain.model.Role;
import com.pragma.usersMicroservice.domain.model.User;
import com.pragma.usersMicroservice.domain.spi.IJwtProviderPort;
import com.pragma.usersMicroservice.domain.spi.IPasswordEncryptionPort;
import com.pragma.usersMicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersMicroservice.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Use case for handling user authentication.
 */
@Slf4j
@RequiredArgsConstructor
public class AuthenticationUseCase implements IAuthServicePort {
    private final IPasswordEncryptionPort passwordEncryptionPort;
    private final IJwtProviderPort jwtProviderPort;
    private final IUserPersistencePort userPersistencePort;
    private final IRolePersistencePort rolePersistencePort;

    /**
     * Authenticates a user and generates a JWT token if the credentials are valid.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return A JWT token if authentication is successful.
     * @throws InvalidCredentialsException If the credentials are invalid.
     */
    @Override
    public String login(String email, String password) {
        Optional<User> user = userPersistencePort.findByEmail(email);

        if (user.isEmpty() || !passwordEncryptionPort.matches(password, user.get().getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        Role role = rolePersistencePort.findById(Long.parseLong(user.get().getRole().getId())).orElseThrow(
                () -> new InvalidCredentialsException("Role not found for user")
        );

        user.get().setRole(role);
        return jwtProviderPort.generateToken(user.get());
    }

    @Override
    public UserValidationResponse validateNewToken(String token) {


        //If token is valid and user exists, generate a new token, otherwise throw an exception
        jwtProviderPort.validateToken(token);


        //Get user from token and check if user exists
        User user = userPersistencePort.findByEmail(jwtProviderPort.getEmailFromToken(token)).orElseThrow(
                () -> new InvalidCredentialsException("User not found for token")
        );


        //Get role from user and compare it with the role in the token, if they don't match throw an exception
        Role role = rolePersistencePort.findById(Long.parseLong(user.getRole().getId())).orElseThrow(
                () -> new InvalidCredentialsException("Role not found for user")
        );
        if (!role.getName().toString().equals(jwtProviderPort.getRoleFromToken(token))) {
            throw new InvalidCredentialsException("Invalid role for token");
        }

        return UserValidationResponse.builder()
                .id(user.getId())
                .role(role.getName().toString())
                .email(user.getEmail())
                .build();
    }
}
