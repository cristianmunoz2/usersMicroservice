package com.pragma.usersMicroservice.infrastructure.output.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Persistence entity representing a user in the database.
 * Maps the domain User model to the 'users' table.
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(unique = true, nullable = false, length = 20)
    private String idDocument;

    @Column(length = 15)
    private String phone;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "role_id", nullable = false)
    private Long roleId;
}