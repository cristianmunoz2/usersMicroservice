package com.pragma.usersMicroservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for user registration requests.
 * Carries the necessary information to create a new user in the system.
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserRegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Lastname is required")
    private String lastName;

    //Document validations
    @NotBlank(message = "ID document is required")
    @Pattern(regexp = "\\d+", message = "ID document must be numeric")
    private String idDocument;

    //Phone validations
    @NotBlank(message = "Phone number is required")
    @Size(max = 13, message = "Phone number must be at most 13 characters")
    @Pattern(regexp = "^\\+?\\d+$", message = "Phone must be numeric and can include a leading '+'")
    private String phone;

    @NotNull(message = "Birthday is required")
    private LocalDate birthDate;

    //Email validations
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
