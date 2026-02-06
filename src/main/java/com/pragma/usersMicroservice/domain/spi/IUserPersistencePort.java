package com.pragma.usersMicroservice.domain.spi;

import com.pragma.usersMicroservice.domain.model.User;

public interface IUserPersistencePort {
    User saveUser(User user);
}
