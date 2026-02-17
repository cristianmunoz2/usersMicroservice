package com.pragma.usersMicroservice.domain.usecase;

import com.pragma.usersMicroservice.domain.exception.EmailAlreadyExistsException;
import com.pragma.usersMicroservice.domain.exception.IdDocAlreadyExistsException;
import com.pragma.usersMicroservice.domain.exception.UnderAgeException;
import com.pragma.usersMicroservice.infrastructure.exception.RoleNotFoundException;
import com.pragma.usersMicroservice.domain.model.Role;
import com.pragma.usersMicroservice.domain.model.User;
import com.pragma.usersMicroservice.domain.spi.IPasswordEncryptionPort;
import com.pragma.usersMicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersMicroservice.domain.spi.IUserPersistencePort;
import com.pragma.usersMicroservice.domain.util.RoleName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IRolePersistencePort rolePersistencePort;

    @Mock
    private IPasswordEncryptionPort passwordEncryptionPort;

    @InjectMocks
    private UserUseCase userUseCase;

    // --- Happy Path Tests ---

    @Test
    @DisplayName("CreateOwner: Should save user with OWNER role and Encrypted Password")
    void createOwner_shouldSaveUser_whenDataIsValid() {
        // Arrange
        User validUser = buildUser(25);

        Role ownerRole = Role.builder()
                .id("1")
                .name(RoleName.OWNER)
                .description("Owner Role Description")
                .build();

        String encryptedPass = "encodedPass123";

        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByIdDocument(anyString())).thenReturn(false);
        when(rolePersistencePort.findByName(RoleName.OWNER)).thenReturn(Optional.of(ownerRole));
        when(passwordEncryptionPort.encode(anyString())).thenReturn(encryptedPass);

        when(userPersistencePort.saveUser(any(User.class))).thenReturn(validUser);

        // Act
        User result = userUseCase.createOwner(validUser);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userPersistencePort).saveUser(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        assertEquals(encryptedPass, capturedUser.getPassword(), "Password should be encrypted");
        assertEquals(RoleName.OWNER, capturedUser.getRole().getName(), "Role should be OWNER");
        assertNotNull(result);
    }

    @Test
    @DisplayName("CreateEmployee: Should set EMPLOYEE role and call save (void return)")
    void createEmployee_shouldSetEmployeeRole_whenDataIsValid() {
        // Arrange
        User validUser = buildUser(30);

        Role employeeRole = Role.builder()
                .id("2")
                .name(RoleName.EMPLOYEE)
                .description("Employee Role Description")
                .build();

        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByIdDocument(anyString())).thenReturn(false);
        when(rolePersistencePort.findByName(RoleName.EMPLOYEE)).thenReturn(Optional.of(employeeRole));
        when(passwordEncryptionPort.encode(anyString())).thenReturn("xyz");

        // Act
        userUseCase.createEmployee(validUser);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userPersistencePort).saveUser(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals(RoleName.EMPLOYEE, capturedUser.getRole().getName());
    }


    @Test
    @DisplayName("ValidateAge: Should throw UnderAgeException when user is younger than 18")
    void saveUser_shouldThrowUnderAgeException_whenUserIsMinor() {
        // Arrange
        User kidUser = buildUser(17); // 17 años

        // Act & Assert
        assertThrows(UnderAgeException.class, () -> userUseCase.createOwner(kidUser));

        // Verify
        verify(userPersistencePort, never()).saveUser(any(User.class));
    }

    @Test
    @DisplayName("ValidateEmail: Should throw EmailAlreadyExistsException when email exists")
    void saveUser_shouldThrowEmailException_whenEmailExists() {
        // Arrange
        User user = buildUser(20);
        when(userPersistencePort.existsByEmail(user.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> userUseCase.createOwner(user));

        verify(userPersistencePort, never()).saveUser(any(User.class));
    }

    @Test
    @DisplayName("ValidateDoc: Should throw IdDocAlreadyExistsException when document exists")
    void saveUser_shouldThrowIdDocException_whenDocExists() {
        // Arrange
        User user = buildUser(20);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByIdDocument(user.getIdDocument())).thenReturn(true);

        // Act & Assert
        assertThrows(IdDocAlreadyExistsException.class, () -> userUseCase.createOwner(user));

        verify(userPersistencePort, never()).saveUser(any(User.class));
    }

    @Test
    @DisplayName("ValidateRole: Should throw RoleNotFoundException if role is not in DB")
    void saveUser_shouldThrowRoleNotFound_whenRoleIsMissing() {
        // Arrange
        User user = buildUser(20);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByIdDocument(anyString())).thenReturn(false);

        when(rolePersistencePort.findByName(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoleNotFoundException.class, () -> userUseCase.createOwner(user));

        verify(userPersistencePort, never()).saveUser(any(User.class));
    }


    // --- Helper Method ---

    private User buildUser(int age) {
        return User.builder()
                .name("Test")
                .lastName("User")
                .email("test@mail.com")
                .phone("+573001234567")
                .idDocument("100200300")
                .password("plainPassword")
                .birthDate(LocalDate.now().minusYears(age))
                .role(null)
                .build();
    }
}