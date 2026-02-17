package com.pragma.usersMicroservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    // --- Happy Path ---

    @Test
    @DisplayName("Should create User successfully when all fields are valid")
    void build_shouldCreateUser_whenDataIsValid() {
        // Arrange & Act
        User user = User.builder()
                .name("John")
                .lastName("Doe")
                .email("john.doe@test.com")
                .phone("+573001234567") // 13 chars max, numeric with +
                .idDocument("123456789") // Numeric
                .birthDate(LocalDate.of(1990, 1, 1))
                .password("securePass")
                .build();

        // Assert
        assertNotNull(user);
        assertEquals("john.doe@test.com", user.getEmail());
        assertEquals("+573001234567", user.getPhone());
    }

    // --- Null Validations ---

    @Test
    @DisplayName("Should throw exception when mandatory fields are null")
    void build_shouldThrowException_whenFieldsAreNull() {
        // Arrange - Builder with null email explicitly or implicitly
        User.UserBuilder builder = User.builder()
                .name("Test")
                .phone("+12345")
                .idDocument("12345")
                .email(null); // Explicit null

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, builder::build);
        assertEquals("Email, Phone, and ID Document cannot be null", exception.getMessage());
    }

    // --- Email Validation ---

    @ParameterizedTest
    @ValueSource(strings = {"plainaddress", "#@%^%#$@#$@#.com", "@example.com", "Joe Smith <email@example.com>"})
    @DisplayName("Should throw exception for invalid email formats")
    void build_shouldThrowException_whenEmailIsInvalid(String invalidEmail) {
        User.UserBuilder builder = User.builder()
                .phone("1234567890")
                .idDocument("123456789")
                .email(invalidEmail);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, builder::build);
        assertEquals("Invalid email format", exception.getMessage());
    }

    // --- Phone Validation ---

    @ParameterizedTest
    @ValueSource(strings = {
            "12345678901234",  // Too long (>13)
            "phone123",        // Contains letters
            "123-456-7890",    // Contains dashes (regex only allows digits and +)
            ""                 // Empty string (regex requires at least one digit)
    })
    @DisplayName("Should throw exception for invalid phone numbers")
    void build_shouldThrowException_whenPhoneIsInvalid(String invalidPhone) {
        User.UserBuilder builder = User.builder()
                .email("valid@test.com")
                .idDocument("12345")
                .phone(invalidPhone);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, builder::build);

        assertTrue(exception.getMessage().contains("Phone number must be numeric"));
    }

    @Test
    @DisplayName("Should allow phone number with exactly 13 characters including +")
    void build_shouldAllowPhoneWithMaxLenght() {
        String limitPhone = "+123456789012"; // 13 chars
        User user = User.builder()
                .email("valid@test.com")
                .idDocument("12345")
                .phone(limitPhone)
                .build();

        assertNotNull(user);
        assertEquals(limitPhone, user.getPhone());
    }

    // --- ID Document Validation ---

    @ParameterizedTest
    @ValueSource(strings = {"123A456", "ID-123", "12.345", ""})
    @DisplayName("Should throw exception when ID Document is not numeric")
    void build_shouldThrowException_whenIdDocumentIsNotNumeric(String invalidId) {
        User.UserBuilder builder = User.builder()
                .email("valid@test.com")
                .phone("1234567890")
                .idDocument(invalidId);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, builder::build);
        assertEquals("ID document must be numeric", exception.getMessage());
    }
}