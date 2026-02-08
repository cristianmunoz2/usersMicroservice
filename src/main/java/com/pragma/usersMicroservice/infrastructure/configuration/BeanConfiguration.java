package com.pragma.usersMicroservice.infrastructure.configuration;

import com.pragma.usersMicroservice.domain.api.IUserServicePort;
import com.pragma.usersMicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersMicroservice.domain.spi.IUserPersistencePort;
import com.pragma.usersMicroservice.domain.usecase.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for wiring domain dependencies.
 * <p>
 * This class uses Spring's IoC container to inject the necessary persistence adapters
 * into the domain use cases, keeping the domain isolated from the framework.
 * </p>
 */
@Configuration
public class BeanConfiguration {

    /**
     * Registers the {@link IUserServicePort} bean.
     * <p>
     * Injects the required persistence ports into the {@link UserUseCase}.
     * </p>
     *
     * @param userPersistencePort Output port for user persistence operations.
     * @param rolePersistencePort Output port for role persistence operations.
     * @return A configured instance of UserUseCase.
     */
    @Bean
    public IUserServicePort userServicePort(IUserPersistencePort userPersistencePort,
                                            IRolePersistencePort rolePersistencePort){
        return new UserUseCase(userPersistencePort, rolePersistencePort);
    }
}