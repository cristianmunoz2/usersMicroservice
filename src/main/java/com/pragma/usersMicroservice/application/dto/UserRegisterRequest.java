package com.pragma.usersMicroservice.application.dto;

import com.pragma.usersMicroservice.domain.model.Role;
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
    private LocalDate birthDate;
    private String email;
    private String password;
}
