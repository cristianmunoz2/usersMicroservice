package com.pragma.usersMicroservice.infrastructure.output.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * JPA Entity representing the 'users' table in the database.
 * <p>
 * Contains the physical data mapping for user information, including
 * unique constraints and column definitions.
 * </p>
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {

    /**
     * Primary key. Auto-incremented identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * User's first name. Mandatory field, max 50 characters.
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * User's last name. Mandatory field, max 50 characters.
     */
    @Column(nullable = false, length = 50)
    private String lastName;

    /**
     * Unique identity document number.
     * Cannot be duplicated in the system.
     */
    @Column(unique = true, nullable = false, length = 20)
    private String idDocument;

    /**
     * Contact phone number.
     */
    @Column(length = 15)
    private String phone;

    /**
     * User's date of birth. Mandatory.
     */
    @Column(nullable = false)
    private LocalDate birthDate;

    /**
     * Unique email address. Used as a system identifier or login.
     */
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    /**
     * User's password (typically encrypted).
     */
    @Column(nullable = false)
    private String password;

    /**
     * Foreign key reference to the role ID.
     * Maps the relationship to the roles table.
     */
    @Column(name = "id_role", nullable = false)
    private Long roleId;
}