package com.pragma.usersMicroservice.domain.api;

import com.pragma.usersMicroservice.domain.model.User;

public interface IUserServicePort {
    User createOwner(User user);
}
