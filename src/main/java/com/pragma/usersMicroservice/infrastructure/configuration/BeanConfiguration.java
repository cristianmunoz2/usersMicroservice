package com.pragma.usersMicroservice.infrastructure.configuration;

import com.pragma.usersMicroservice.domain.api.IUserServicePort;
import com.pragma.usersMicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersMicroservice.domain.spi.IUserPersistencePort;
import com.pragma.usersMicroservice.domain.usecase.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public IUserServicePort userServicePort(IUserPersistencePort userPersistencePort,
                                            IRolePersistencePort rolePersistencePort){
        return new UserUseCase(userPersistencePort, rolePersistencePort);
    }
}
