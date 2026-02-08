package com.pragma.usersMicroservice.application.handler;

import com.pragma.usersMicroservice.application.dto.UserRegisterRequest;
import com.pragma.usersMicroservice.domain.model.User;

public interface IUserHandler {
    void createOwner(UserRegisterRequest userRegisterRequest);
}
