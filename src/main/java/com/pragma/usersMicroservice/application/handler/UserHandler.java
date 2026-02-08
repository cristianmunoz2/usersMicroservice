package com.pragma.usersMicroservice.application.handler;

import com.pragma.usersMicroservice.application.dto.UserRegisterRequest;
import com.pragma.usersMicroservice.application.mapper.IUserRegisterRequestMapper;
import com.pragma.usersMicroservice.domain.api.IUserServicePort;
import com.pragma.usersMicroservice.domain.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler{

    private final IUserServicePort userServicePort;
    private final IUserRegisterRequestMapper userRegisterRequestMapper;

    @Override
    public void createOwner(UserRegisterRequest userRegisterRequest) {
        User newOwner = userRegisterRequestMapper.toUser(userRegisterRequest);
        userServicePort.createOwner(newOwner);
    }
}
