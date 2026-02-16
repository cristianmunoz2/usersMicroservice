package com.pragma.usersMicroservice.application.handler;

import com.pragma.usersMicroservice.application.dto.JwtResponse;
import com.pragma.usersMicroservice.application.dto.UserRegisterRequest;

/**
 * Interface that defines the contract for user operations.
 * Acts as an orchestrator between the REST controller and the domain use cases.
 */
public interface IUserHandler {

    /**
     * Handles the logic to register a new user with the Owner role.
     *
     * @param userRegisterRequest The DTO containing the user's registration data.
     */
    void createOwner(UserRegisterRequest userRegisterRequest);

    /**
     * Handles the logic to register a new user with the Employee role.
     *
     * @param userRegisterRequest The DTO containing the user's registration data.
     */
    void createEmployee(UserRegisterRequest userRegisterRequest);

    /**
     * Handles the logic to register a new user with the Customer role.
     *
     * @param userRegisterRequest The DTO containing the user's registration data.
     */
    JwtResponse createCustomer(UserRegisterRequest userRegisterRequest);
}