package com.pragma.usersmicroservice.domain.usecase;

import com.pragma.usersmicroservice.application.dto.response.UserValidationResponse;
import com.pragma.usersmicroservice.domain.exception.InvalidCredentialsException;
import com.pragma.usersmicroservice.domain.model.Role;
import com.pragma.usersmicroservice.domain.model.User;
import com.pragma.usersmicroservice.domain.spi.IJwtProviderPort;
import com.pragma.usersmicroservice.domain.spi.IPasswordEncryptionPort;
import com.pragma.usersmicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersmicroservice.domain.spi.IUserPersistencePort;
import com.pragma.usersmicroservice.domain.util.RoleName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationUseCaseTest {

    @Mock
    private IPasswordEncryptionPort passwordEncryptionPort;
    @Mock
    private IJwtProviderPort jwtProviderPort;
    @Mock
    private IUserPersistencePort userPersistencePort;
    @Mock
    private IRolePersistencePort rolePersistencePort;

    @InjectMocks
    private AuthenticationUseCase authenticationUseCase;


    @Test
    @DisplayName("Login: Should return token when credentials are valid")
    void login_shouldReturnToken_whenCredentialsAreValid() {
        // Arrange
        String email = "test@mail.com";
        String password = "password123";
        String encryptedPass = "encryptedPass";
        String token = "valid.jwt.token";
        String phone = "+573001234567";
        String idDocument = "123456789";

        Role role = Role.builder()
                .id("1")
                .name(RoleName.OWNER)
                .description("Owner Role")
                .build();

        User user = User.builder()
                .id("100")
                .email(email)
                .password(encryptedPass)
                .role(role)
                .phone(phone)
                .idDocument(idDocument)
                .build();

        when(userPersistencePort.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncryptionPort.matches(password, encryptedPass)).thenReturn(true);
        when(rolePersistencePort.findById(1L)).thenReturn(Optional.of(role));
        when(jwtProviderPort.generateToken(user)).thenReturn(token);

        // Act
        String result = authenticationUseCase.login(email, password);

        // Assert
        assertEquals(token, result);
        verify(jwtProviderPort).generateToken(user);
    }

    @Test
    @DisplayName("Login: Should throw exception when user not found")
    void login_shouldThrowException_whenUserNotFound() {
        // Arrange
        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () ->
                authenticationUseCase.login("wrong@mail.com", "1234"));

        verify(passwordEncryptionPort, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Login: Should throw exception when password does not match")
    void login_shouldThrowException_whenPasswordInvalid() {
        // Arrange
        User user = User.builder()
                .email("test@mail.com")
                .password("encrypted")
                .phone("+573001234567")
                .idDocument("123456789")
                .build();

        when(userPersistencePort.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncryptionPort.matches("wrongPass", "encrypted")).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () ->
                authenticationUseCase.login("test@mail.com", "wrongPass"));

        verify(rolePersistencePort, never()).findById(anyLong());
        verify(jwtProviderPort, never()).generateToken(any());
    }

    @Test
    @DisplayName("Login: Should throw exception when role not found in DB")
    void login_shouldThrowException_whenRoleNotFound() {
        // Arrange
        Role roleRef = Role.builder().id("1").name(RoleName.OWNER).build();
        User user = User.builder()
                .email("test@mail.com")
                .password("pass")
                .role(roleRef)
                .phone("+543145689785")
                .idDocument("123456789")
                .build();

        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncryptionPort.matches(anyString(), anyString())).thenReturn(true);

        when(rolePersistencePort.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        InvalidCredentialsException ex = assertThrows(InvalidCredentialsException.class, () ->
                authenticationUseCase.login("test@mail.com", "pass"));

        assertEquals("Role not found for user", ex.getMessage());
    }


    @Test
    @DisplayName("ValidateNewToken: Should return response when token and role are valid")
    void validateNewToken_shouldReturnResponse_whenValid() {
        // Arrange
        String token = "validToken";
        String email = "test@mail.com";

        Role role = Role.builder()
                .id("2")
                .name(RoleName.CUSTOMER)
                .description("Customer Role")
                .build();

        User user = User.builder()
                .id("100")
                .email(email)
                .role(role)
                .phone("+573001234567")
                .idDocument("123456789")
                .build();

        when(jwtProviderPort.validateToken(token)).thenReturn(true);
        when(jwtProviderPort.getEmailFromToken(token)).thenReturn(email);
        when(userPersistencePort.findByEmail(email)).thenReturn(Optional.of(user));
        when(rolePersistencePort.findById(2L)).thenReturn(Optional.of(role));
        when(jwtProviderPort.getRoleFromToken(token)).thenReturn("CUSTOMER");

        // Act
        UserValidationResponse response = authenticationUseCase.validateNewToken(token);

        // Assert
        assertNotNull(response);
        assertEquals("100", response.getId());
        assertEquals(email, response.getEmail());
        assertEquals("CUSTOMER", response.getRole());
    }

    @Test
    @DisplayName("ValidateNewToken: Should throw exception when user not found")
    void validateNewToken_shouldThrowException_whenUserNotFound() {
        // Arrange
        String token = "token";
        when(jwtProviderPort.getEmailFromToken(token)).thenReturn("unknown@mail.com");
        when(userPersistencePort.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());

        // Act & Assert
        InvalidCredentialsException ex = assertThrows(InvalidCredentialsException.class, () ->
                authenticationUseCase.validateNewToken(token));

        assertEquals("User not found for token", ex.getMessage());
    }

    @Test
    @DisplayName("ValidateNewToken: Should throw exception when role mismatch")
    void validateNewToken_shouldThrowException_whenRoleMismatch() {
        // Arrange
        String token = "token";

        Role dbRole = Role.builder()
                .id("1")
                .name(RoleName.CUSTOMER)
                .build();

        User user = User.builder().id("1")
                .email("test@mail.com")
                .role(dbRole)
                .phone("+573001234567")
                .idDocument("123456789")
                .build();

        when(jwtProviderPort.getEmailFromToken(token)).thenReturn("test@mail.com");
        when(userPersistencePort.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(rolePersistencePort.findById(1L)).thenReturn(Optional.of(dbRole));

        when(jwtProviderPort.getRoleFromToken(token)).thenReturn("OWNER");

        // Act & Assert
        InvalidCredentialsException ex = assertThrows(InvalidCredentialsException.class, () ->
                authenticationUseCase.validateNewToken(token));

        assertEquals("Invalid role for token", ex.getMessage());
    }
}