package com.pragma.usersmicroservice.domain.usecase;

import com.pragma.usersmicroservice.application.dto.response.UserValidationResponse;
import com.pragma.usersmicroservice.domain.api.IAuthServicePort;
import com.pragma.usersmicroservice.domain.exception.InvalidCredentialsException;
import com.pragma.usersmicroservice.domain.model.Role;
import com.pragma.usersmicroservice.domain.model.User;
import com.pragma.usersmicroservice.domain.spi.IJwtProviderPort;
import com.pragma.usersmicroservice.domain.spi.IPasswordEncryptionPort;
import com.pragma.usersmicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersmicroservice.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    private static final String INVALID_CREDENTIALS = "Invalid credentials";

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
        User user = userPersistencePort.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException(INVALID_CREDENTIALS));

        if (!passwordEncryptionPort.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS);
        }

        Role role = rolePersistencePort.findById(Long.parseLong(user.getRole().getId()))
                .orElseThrow(() -> new InvalidCredentialsException(INVALID_CREDENTIALS));

        User authenticatedUser = User.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .idDocument(user.getIdDocument())
                .phone(user.getPhone())
                .birthDate(user.getBirthDate())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(role)
                .build();

        return jwtProviderPort.generateToken(authenticatedUser);
    }

    @Override
    public UserValidationResponse validateNewToken(String token) {
        if (!jwtProviderPort.validateToken(token)) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS);
        }

        String email = jwtProviderPort.getEmailFromToken(token);
        User user = userPersistencePort.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException(INVALID_CREDENTIALS));

        Role role = rolePersistencePort.findById(Long.parseLong(user.getRole().getId()))
                .orElseThrow(() -> new InvalidCredentialsException(INVALID_CREDENTIALS));

        String tokenRole = jwtProviderPort.getRoleFromToken(token);
        if (!role.getName().toString().equals(tokenRole)) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS);
        }

        return UserValidationResponse.builder()
                .userId(user.getId())
                .userRole(role.getName().toString())
                .userEmail(user.getEmail())
                .build();
    }
}
