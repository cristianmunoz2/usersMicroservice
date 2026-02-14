package com.pragma.usersMicroservice.infrastructure.input.rest;

import com.pragma.usersMicroservice.application.dto.JwtResponse;
import com.pragma.usersMicroservice.application.dto.UserRegisterRequest;
import com.pragma.usersMicroservice.application.handler.IUserHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for managing user-related operations.
 * <p>
 * Exposes endpoints to interact with the user microservice via HTTP.
 * </p>
 */
@RestController
@RequestMapping("/users/")
@RequiredArgsConstructor
public class UserRestController {

    private final IUserHandler userHandler;

    /**
     * Registers a new user with the 'Owner' role.
     * <p>
     * Validates the request body and delegates execution to the application handler.
     * </p>
     *
     * @param userRegisterRequest The DTO containing the user's registration details.
     * @return A {@link ResponseEntity} with HTTP status 201 (Created).
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registerOwner")
    public ResponseEntity<Void> registerOwner(@Valid @RequestBody UserRegisterRequest userRegisterRequest){
        userHandler.createOwner(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Registers a new user with the 'Employee' role.
     * <p>
     * Validates the request body and delegates execution to the application handler.
     * </p>
     *
     * @param userRegisterRequest The DTO containing the user's registration details.
     * @return A {@link ResponseEntity} with HTTP status 201 (Created).
     */
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/registerEmployee")
    public ResponseEntity<Void> registerEmployee(@Valid @RequestBody UserRegisterRequest userRegisterRequest){
        userHandler.createEmployee(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Registers a new user with the 'Customer' role.
     * <p>
     * Validates the request body and delegates execution to the application handler.
     * </p>
     *
     * @param userRegisterRequest The DTO containing the user's registration details.
     * @return A {@link ResponseEntity} with HTTP status 201 (Created) and the generated token.
     */
    @PostMapping("/registerCustomer")
    public ResponseEntity<JwtResponse> registerCustomer(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        JwtResponse response = userHandler.createCustomer(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}