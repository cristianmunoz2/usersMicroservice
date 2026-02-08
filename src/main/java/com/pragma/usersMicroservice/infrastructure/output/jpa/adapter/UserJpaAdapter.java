package com.pragma.usersMicroservice.infrastructure.output.jpa.adapter;

import com.pragma.usersMicroservice.domain.model.User;
import com.pragma.usersMicroservice.domain.spi.IUserPersistencePort;
import com.pragma.usersMicroservice.infrastructure.output.jpa.entity.UserEntity;
import com.pragma.usersMicroservice.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.pragma.usersMicroservice.infrastructure.output.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * JPA Adapter implementation for User persistence.
 * <p>
 * Implements the {@link IUserPersistencePort} to manage user data operations
 * using Spring Data JPA, handling the mapping between domain models and entities.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    /**
     * Persists a User domain object into the database.
     *
     * @param user The User domain model to be saved.
     * @return The persisted User domain object (including generated attributes like ID).
     */
    @Override
    public User saveUser(User user) {
        UserEntity userEntity = userRepository.save(userEntityMapper.toEntity(user));
        return userEntityMapper.toUser(userEntity);
    }
}