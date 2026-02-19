package com.pragma.usersMicroservice.application.handler.impl;

import com.pragma.usersMicroservice.application.dto.JwtResponse;
import com.pragma.usersMicroservice.application.dto.PhoneByIdResponse;
import com.pragma.usersMicroservice.application.dto.UserRegisterRequest;
import com.pragma.usersMicroservice.application.handler.IUserHandler;
import com.pragma.usersMicroservice.application.mapper.IUserRegisterRequestMapper;
import com.pragma.usersMicroservice.domain.api.IAuthServicePort;
import com.pragma.usersMicroservice.domain.api.IUserServicePort;
import com.pragma.usersMicroservice.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for handling user-related operations.
 * <p>
 * This class acts as an orchestrator in the application layer, bridging the gap
 * between the incoming REST requests (DTOs) and the core business logic (Domain Layer).
 * It handles data transformation and transaction management.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserHandlerImpl implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final IAuthServicePort authServicePort;
    private final IUserRegisterRequestMapper userRegisterRequestMapper;

    /**
     * Orchestrates the process of creating a new user with the 'Owner' role.
     * <p>
     * 1. Converts the {@link UserRegisterRequest} DTO into a {@link User} domain entity.
     * 2. Delegates the business logic to the {@link IUserServicePort}.
     * </p>
     *
     * @param userRegisterRequest The data transfer object containing the registration details.
     */
    @Override
    public void createOwner(UserRegisterRequest userRegisterRequest) {
        User newOwner = userRegisterRequestMapper.toUser(userRegisterRequest);
        userServicePort.createOwner(newOwner);
    }

    /**
     * Orchestrates the process of creating a new user with the 'Employee' role.
     * <p>
     * 1. Converts the {@link UserRegisterRequest} DTO into a {@link User} domain entity.
     * 2. Delegates the business logic to the {@link IUserServicePort}.
     * </p>
     *
     * @param userRegisterRequest The data transfer object containing the registration details.
     */
    public void createEmployee(UserRegisterRequest userRegisterRequest) {
        User newEmployee = userRegisterRequestMapper.toUser(userRegisterRequest);
        userServicePort.createEmployee(newEmployee);
    }

    /**
     * Orchestrates the process of creating a new user with the 'Customer' role.
     * <p>
     * 1. Converts the {@link UserRegisterRequest} DTO into a {@link User} domain entity.
     * 2. Delegates the business logic to the {@link IUserServicePort}.
     * </p>
     *
     * @param userRegisterRequest The data transfer object containing the registration details.
     */
    @Override
    public JwtResponse createCustomer(UserRegisterRequest userRegisterRequest) {
        User user = userRegisterRequestMapper.toUser(userRegisterRequest);
        userServicePort.createCustomer(user);
        String token = authServicePort.login(userRegisterRequest.getEmail(), userRegisterRequest.getPassword());
        return JwtResponse.builder().token(token).build();
    }

    @Override
    public boolean existsById(String id) {
        return userServicePort.existsById(id);
    }

    @Override
    public PhoneByIdResponse getPhoneById(String email) {
        PhoneByIdResponse response = new PhoneByIdResponse();
        response.setPhoneNumber(userServicePort.getPhoneById(email));
        return response;
    }

}