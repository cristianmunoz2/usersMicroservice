package com.pragma.usersMicroservice.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Data Transfer Object for user registration requests.
 * Carries the necessary information to create a new user in the system.
 */
@Data
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    private String name;
    private String lastName;
    private String idDocument;
    private String phone;
    @NotNull(message = "Birthday is required")
    private LocalDate birthDate;
    private String email;
    private String password;
}
