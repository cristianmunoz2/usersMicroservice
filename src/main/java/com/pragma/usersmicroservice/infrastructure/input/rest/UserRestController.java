package com.pragma.usersmicroservice.infrastructure.input.rest;

import com.pragma.usersmicroservice.application.dto.response.EmailByIdResponse;
import com.pragma.usersmicroservice.application.dto.response.JwtResponse;
import com.pragma.usersmicroservice.application.dto.response.PhoneByIdResponse;
import com.pragma.usersmicroservice.application.dto.request.UserRegisterRequest;
import com.pragma.usersmicroservice.application.handler.IUserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing user-related operations.
 * <p>
 * Exposes endpoints to interact with the user microservice via HTTP.
 * </p>
 */
@Tag(name = "Users", description = "User management endpoints")
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
    @Operation(summary = "Register owner", description = "Registers a new user with Owner role (Admin only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponse(responseCode = "201", description = "Owner created successfully")
    @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    @ApiResponse(responseCode = "409", description = "Email or ID already exists", content = @Content)
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
    @Operation(summary = "Register employee", description = "Registers a new user with Employee role (Owner only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponse(responseCode = "201", description = "Employee created successfully")
    @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    @ApiResponse(responseCode = "409", description = "Email or ID already exists", content = @Content)
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
    @Operation(summary = "Register customer", description = "Registers a new user with Customer role (Public endpoint)")
    @ApiResponse(responseCode = "201", description = "Customer created successfully")
    @ApiResponse(responseCode = "400", description = "Validation error or underage user", content = @Content)
    @ApiResponse(responseCode = "409", description = "Email or ID already exists", content = @Content)
    @PostMapping("/registerCustomer")
    public ResponseEntity<JwtResponse> registerCustomer(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        JwtResponse response = userHandler.createCustomer(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Check user existence", description = "Checks if a user exists by ID")
    @ApiResponse(responseCode = "200", description = "User existence status returned")
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable String id) {
        boolean exists = userHandler.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Get phone by ID", description = "Retrieves user phone number by user ID")
    @ApiResponse(responseCode = "200", description = "Phone retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    @GetMapping("/phone")
    public ResponseEntity<PhoneByIdResponse> getPhoneByEmail(@RequestParam("id") String id) {
        PhoneByIdResponse response = userHandler.getPhoneById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get email by ID", description = "Retrieves user email by user ID")
    @ApiResponse(responseCode = "200", description = "Email retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    @GetMapping("/email/{id}")
    public ResponseEntity<EmailByIdResponse> getEmailById(@PathVariable String id) {
        EmailByIdResponse response = userHandler.getEmailById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Check owner existence", description = "Check if an Owner with a given Id exists")
    @ApiResponse(responseCode = "200", description = "Owner existence status returned")
    @GetMapping("/ownerExists/{id}")
    public ResponseEntity<Boolean> existsByOwnerId(@PathVariable String id) {
        boolean exists = userHandler.existsById(id);
        return ResponseEntity.ok(exists);
    }
}