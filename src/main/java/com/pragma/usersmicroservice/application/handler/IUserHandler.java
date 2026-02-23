package com.pragma.usersmicroservice.application.handler;

import com.pragma.usersmicroservice.application.dto.response.EmailByIdResponse;
import com.pragma.usersmicroservice.application.dto.response.JwtResponse;
import com.pragma.usersmicroservice.application.dto.response.PhoneByIdResponse;
import com.pragma.usersmicroservice.application.dto.request.UserRegisterRequest;

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

    /**
     * Checks if a user with the given ID exists in the system.
     *
     * @param id The unique identifier of the user.
     * @return true if the user exists, false otherwise.
     */
    boolean existsById(String id);

    /**
     * Gets the phone number of a user by their id.
     *
     * @param id The id of the user.
     * @return The phone number of the user.
     */
    PhoneByIdResponse getPhoneById(String id);

    EmailByIdResponse getEmailById(String id);

    boolean ownerExistsById(String id);
    boolean employeeExistsById(String id);
}