package com.pragma.usersMicroservice.domain.model;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Represents a User within the application's domain model.
 */
public class User {
    private String id;
    private String name;
    private String lastName;
    private String idDocument;
    private String phone;
    private LocalDate birthDate;
    private String email;
    private String password;
    private Role role;

    // --- Constants for Validation ---

    /**
     * Regex to validate standard email formats.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    /**
     * Regex to validate phone numbers. Allows an optional leading '+' and requires digits.
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d+$");

    /**
     * Regex to validate that the ID document contains only numeric characters.
     */
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^\\d+$");


    /**
     * Private constructor to enforce the use of the Builder pattern.
     * <p>
     * Validates business rules (format and length) before assigning values to ensure
     * the entity is always in a valid state.
     * </p>
     *
     * @param userBuilder The builder instance containing the user data.
     * @throws IllegalArgumentException if the provided data violates domain rules.
     */
    public User(UserBuilder userBuilder) {
        // Validation logic is executed before assignment
        validateFormat(userBuilder.email, userBuilder.phone, userBuilder.idDocument);

        this.id = userBuilder.id;
        this.name = userBuilder.name;
        this.lastName = userBuilder.lastName;
        this.idDocument = userBuilder.idDocument;
        this.phone = userBuilder.phone;
        this.birthDate = userBuilder.birthDate;
        this.email = userBuilder.email;
        this.password = userBuilder.password;
        this.role = userBuilder.role;
    }

    /**
     * Validates that the input data conforms to the required business formats.
     * @param email      The email address to validate.
     * @param phone      The phone number to validate.
     * @param idDocument The identity document to validate.
     * @throws IllegalArgumentException if any format is invalid or fields are null.
     */
    private void validateFormat(String email, String phone, String idDocument) {
        // 1. Safety check for null values to avoid NullPointerException
        if (email == null || phone == null || idDocument == null) {
            throw new IllegalArgumentException("Email, Phone, and ID Document cannot be null");
        }

        // 2. Validate Email format
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // 3. Validate Phone: Must be numeric (with optional +) and max 13 characters
        if (!PHONE_PATTERN.matcher(phone).matches() || phone.length() > 13) {
            throw new IllegalArgumentException("Phone number must be numeric, contain a maximum of 13 characters, and can include a '+' sign");
        }

        // 4. Validate ID Document: Must be strictly numeric
        if (!NUMBER_PATTERN.matcher(idDocument).matches()) {
            throw new IllegalArgumentException("ID document must be numeric");
        }
    }

    // --- Getters and Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Initiates the Builder pattern for the User entity.
     * @return A new instance of UserBuilder.
     */
    public static UserBuilder builder() {
        return new UserBuilder();
    }

    /**
     * Builder class for constructing User instances.
     * Uses the Fluent Interface pattern.
     */
    public static class UserBuilder {
        private String id;
        private String name;
        private String lastName;
        private String idDocument;
        private String phone;
        private LocalDate birthDate;
        private String email;
        private String password;
        private Role role;

        public UserBuilder id(String id) {
            this.id = id;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder idDocument(String idDocument) {
            this.idDocument = idDocument;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        /**
         * Finalizes the construction of the User object.
         * @return A new, validated User instance.
         */
        public User build() {
            return new User(this);
        }
    }
}