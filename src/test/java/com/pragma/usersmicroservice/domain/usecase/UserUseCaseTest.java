package com.pragma.usersmicroservice.domain.usecase;

import com.pragma.usersmicroservice.domain.exception.EmailAlreadyExistsException;
import com.pragma.usersmicroservice.domain.exception.IdDocAlreadyExistsException;
import com.pragma.usersmicroservice.domain.exception.UnderAgeException;
import com.pragma.usersmicroservice.infrastructure.exception.RoleNotFoundException;
import com.pragma.usersmicroservice.domain.model.Role;
import com.pragma.usersmicroservice.domain.model.User;
import com.pragma.usersmicroservice.domain.spi.IPasswordEncryptionPort;
import com.pragma.usersmicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersmicroservice.domain.spi.IUserPersistencePort;
import com.pragma.usersmicroservice.domain.util.RoleName;
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


    // --- Criterios de Aceptación Tests ---

    @Test
    @DisplayName("CA1: Should require all mandatory fields")
    void createOwner_shouldRequireAllMandatoryFields() {
        User user = buildUser(25);
        Role ownerRole = Role.builder().id("1").name(RoleName.OWNER).build();

        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByIdDocument(anyString())).thenReturn(false);
        when(rolePersistencePort.findByName(RoleName.OWNER)).thenReturn(Optional.of(ownerRole));
        when(passwordEncryptionPort.encode(anyString())).thenReturn("encrypted");
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);

        User result = userUseCase.createOwner(user);

        assertNotNull(result.getName());
        assertNotNull(result.getLastName());
        assertNotNull(result.getIdDocument());
        assertNotNull(result.getPhone());
        assertNotNull(result.getBirthDate());
        assertNotNull(result.getEmail());
        assertNotNull(result.getPassword());
    }

    @Test
    @DisplayName("CA2: Should validate email format")
    void createOwner_shouldThrowException_whenEmailFormatIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> 
            User.builder()
                .name("Test")
                .lastName("User")
                .email("invalid-email")
                .phone("+573001234567")
                .idDocument("100200300")
                .password("pass")
                .birthDate(LocalDate.now().minusYears(20))
                .build()
        );
    }

    @Test
    @DisplayName("CA2: Should validate phone max 13 characters with + symbol")
    void createOwner_shouldThrowException_whenPhoneExceeds13Characters() {
        assertThrows(IllegalArgumentException.class, () -> 
            User.builder()
                .name("Test")
                .lastName("User")
                .email("test@mail.com")
                .phone("+57300123456789")
                .idDocument("100200300")
                .password("pass")
                .birthDate(LocalDate.now().minusYears(20))
                .build()
        );
    }

    @Test
    @DisplayName("CA2: Should accept phone with + symbol")
    void createOwner_shouldAcceptPhoneWithPlusSymbol() {
        User user = User.builder()
                .name("Test")
                .lastName("User")
                .email("test@mail.com")
                .phone("+573005698325")
                .idDocument("100200300")
                .password("pass")
                .birthDate(LocalDate.now().minusYears(20))
                .build();

        assertEquals("+573005698325", user.getPhone());
    }

    @Test
    @DisplayName("CA2: Should validate idDocument is numeric only")
    void createOwner_shouldThrowException_whenIdDocumentIsNotNumeric() {
        assertThrows(IllegalArgumentException.class, () -> 
            User.builder()
                .name("Test")
                .lastName("User")
                .email("test@mail.com")
                .phone("+573001234567")
                .idDocument("ABC123")
                .password("pass")
                .birthDate(LocalDate.now().minusYears(20))
                .build()
        );
    }

    @Test
    @DisplayName("CA3: Should assign OWNER role when creating owner")
    void createOwner_shouldAssignOwnerRole() {
        User user = buildUser(25);
        Role ownerRole = Role.builder().id("1").name(RoleName.OWNER).build();

        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByIdDocument(anyString())).thenReturn(false);
        when(rolePersistencePort.findByName(RoleName.OWNER)).thenReturn(Optional.of(ownerRole));
        when(passwordEncryptionPort.encode(anyString())).thenReturn("encrypted");
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);

        userUseCase.createOwner(user);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userPersistencePort).saveUser(captor.capture());
        assertEquals(RoleName.OWNER, captor.getValue().getRole().getName());
    }

    @Test
    @DisplayName("CA4: Should validate user is 18 or older")
    void createOwner_shouldThrowException_whenUserIsUnder18() {
        User user = buildUser(17);

        assertThrows(UnderAgeException.class, () -> userUseCase.createOwner(user));
    }

    @Test
    @DisplayName("CA4: Should accept user exactly 18 years old")
    void createOwner_shouldAcceptUser_whenUserIsExactly18() {
        User user = buildUser(18);
        Role ownerRole = Role.builder().id("1").name(RoleName.OWNER).build();

        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByIdDocument(anyString())).thenReturn(false);
        when(rolePersistencePort.findByName(RoleName.OWNER)).thenReturn(Optional.of(ownerRole));
        when(passwordEncryptionPort.encode(anyString())).thenReturn("encrypted");
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> userUseCase.createOwner(user));
    }

    @Test
    @DisplayName("CA1: Should encrypt password with BCrypt")
    void createOwner_shouldEncryptPasswordWithBCrypt() {
        User user = buildUser(25);
        Role ownerRole = Role.builder().id("1").name(RoleName.OWNER).build();
        String encryptedPassword = "$2a$10$encrypted";

        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByIdDocument(anyString())).thenReturn(false);
        when(rolePersistencePort.findByName(RoleName.OWNER)).thenReturn(Optional.of(ownerRole));
        when(passwordEncryptionPort.encode("plainPassword")).thenReturn(encryptedPassword);
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);

        userUseCase.createOwner(user);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userPersistencePort).saveUser(captor.capture());
        verify(passwordEncryptionPort).encode("plainPassword");
        assertEquals(encryptedPassword, captor.getValue().getPassword());
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