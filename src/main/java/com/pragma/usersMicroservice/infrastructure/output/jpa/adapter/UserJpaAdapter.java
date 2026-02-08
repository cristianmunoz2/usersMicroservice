package com.pragma.usersMicroservice.infrastructure.output.jpa.adapter;

import com.pragma.usersMicroservice.domain.model.User;
import com.pragma.usersMicroservice.domain.spi.IUserPersistencePort;
import com.pragma.usersMicroservice.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.pragma.usersMicroservice.infrastructure.output.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    @Override
    public User saveUser(User user) {
        userRepository.save(userEntityMapper.toEntity(user));
        return null;
    }
}
