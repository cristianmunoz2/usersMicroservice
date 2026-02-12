package com.pragma.usersMicroservice.infrastructure.configuration;

import com.pragma.usersMicroservice.domain.api.IAuthServicePort;
import com.pragma.usersMicroservice.domain.api.IUserServicePort;
import com.pragma.usersMicroservice.domain.spi.IJwtProviderPort;
import com.pragma.usersMicroservice.domain.spi.IPasswordEncryptionPort;
import com.pragma.usersMicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersMicroservice.domain.spi.IUserPersistencePort;
import com.pragma.usersMicroservice.domain.usecase.AuthenticationUseCase;
import com.pragma.usersMicroservice.domain.usecase.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for application dependency injection.
 * <p>
 * This class assembles the Hexagonal Architecture by injecting Infrastructure Adapters
 * into Domain Use Cases, ensuring the domain remains agnostic of the framework.
 * </p>
 */
@Configuration
public class BeanConfiguration {

    /**
     * Creates the main entry point for User business logic.
     * <p>
     * Wires the {@link UserUseCase} with its required output ports (persistence and security),
     * effectively connecting the Hexagon's core with the outside world.
     * </p>
     *
     * @param userPersistencePort      Adapter for User database operations.
     * @param rolePersistencePort      Adapter for Role database operations.
     * @param passwordEncryptionPort   Adapter for password hashing mechanisms.
     * @return A fully configured instance of the domain service.
     */
    @Bean
    public IUserServicePort userServicePort(IUserPersistencePort userPersistencePort,
                                            IRolePersistencePort rolePersistencePort,
                                            IPasswordEncryptionPort passwordEncryptionPort){
        return new UserUseCase(userPersistencePort, rolePersistencePort, passwordEncryptionPort);
    }

    /**
     * Configures the cryptographic hashing algorithm for the application.
     * <p>
     * Provides a {@link BCryptPasswordEncoder} instance to be injected into security adapters.
     * This defines "how" passwords are secured without the domain knowing the details.
     * </p>
     *
     * @return A standard BCrypt implementation for password hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates the main entry point for Authentication business logic.
     * <p>
     * Wires the {@link AuthenticationUseCase} with its required output ports (persistence, security, and JWT),
     * enabling the domain to perform authentication without direct dependencies on the framework.
     * </p>
     *
     * @param passwordEncryptionPort Adapter for password hashing mechanisms.
     * @param userPersistencePort    Adapter for User database operations.
     * @param jwtProviderPort       Adapter for JWT token generation and validation.
     * @return A fully configured instance of the authentication service.
     */
    @Bean
    public IAuthServicePort authServicePort(IPasswordEncryptionPort passwordEncryptionPort,
                                            IUserPersistencePort userPersistencePort,
                                            IJwtProviderPort jwtProviderPort) {
        return new AuthenticationUseCase(passwordEncryptionPort, jwtProviderPort, userPersistencePort );
    }
}