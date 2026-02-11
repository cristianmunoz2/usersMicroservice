package com.pragma.usersMicroservice.domain.usecase;

import com.pragma.usersMicroservice.domain.exception.EmailAlreadyExistsException;
import com.pragma.usersMicroservice.domain.exception.IdDocAlreadyExistsException;
import com.pragma.usersMicroservice.domain.exception.UnderAgeException;
import com.pragma.usersMicroservice.domain.model.Role;
import com.pragma.usersMicroservice.domain.model.User;
import com.pragma.usersMicroservice.domain.spi.IPasswordEncryptionPort;
import com.pragma.usersMicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersMicroservice.domain.spi.IUserPersistencePort;
import com.pragma.usersMicroservice.domain.util.RoleName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

    //Test Case UT-HU1-001 ----------------------------------------------------------
    @DisplayName("Should save user successfully with Encrypted Password and Owner Role")
    @Test
    void saveUser_shouldCreateUser_whenAllRulesAreMet() {
        //Arrange
        User validUser = new User.UserBuilder()
                .name("Happy")
                .lastName("Path")
                .email("valid@test.com")
                .phone("+573001234567")
                .idDocument("111222333")
                .password("plainPassword")
                .birthDate(LocalDate.now().minusYears(25))
                .role(null)
                .build();

        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByIdDocument(anyString())).thenReturn(false);

        when(passwordEncryptionPort.encode("plainPassword")).thenReturn("encrypted$123");

        Role ownerRole = new Role.RoleBuilder()
                .id("0")
                        .name(RoleName.OWNER)
                                .description("OWNER_ROLE").build();
        when(rolePersistencePort.findByName(RoleName.OWNER)).thenReturn(Optional.of(ownerRole));

        //Act
        userUseCase.saveUser(validUser, RoleName.OWNER);

        //Assert and verify
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userPersistencePort).saveUser(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        //Assert password is encrypted
        assertEquals("encrypted$123", capturedUser.getPassword());

        //Assert User has Owner role
        assertNotNull(capturedUser.getRole());
        assertEquals(RoleName.OWNER, capturedUser.getRole().getName());

        //Assert user was successfully created
        verify(userPersistencePort).existsByEmail("valid@test.com");
    }

    @DisplayName("")


    @DisplayName("An Owner user must be 18 years old at least")
    @Test
    void saveUser_shouldThrowException_whenUserIsUnderAge() {
        User kidUser = new User.UserBuilder()
                .name("Kid")
                .lastName("Test")
                .email("kid@test.com")
                .phone("+573000000000")
                .idDocument("1000000000")
                .password("1234")
                .role(null)
                .birthDate(LocalDate.now().minusYears(10))
                .build();


        assertThrows(UnderAgeException.class, () -> {
            userUseCase.saveUser(kidUser, null);
        });

        verify(userPersistencePort, never()).saveUser(any());
    }


    @DisplayName("Email must be unique")
    @Test
    void saveUser_shouldThrowException_whenEmailAlreadyExists() {
        User existingUser = new User.UserBuilder()
                .name("Existing")
                .lastName("User")
                .email("duplicate@test.com")
                .phone("+573000000000")
                .idDocument("123456789")
                .password("password123")
                .birthDate(LocalDate.now().minusYears(20))
                .role(null)
                .build();

        when(userPersistencePort.existsByEmail("duplicate@test.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> {
            userUseCase.saveUser(existingUser, null);
        });

        verify(userPersistencePort, never()).saveUser(any());
        verify(passwordEncryptionPort, never()).encode(any());
    }


    @DisplayName("Id Document must be unique")
    @Test
    void saveUser_shouldThrownException_ifDocumentAlreadyExists(){
        User existingDocumentUser = new User.UserBuilder()
                .name("Test")
                .lastName("User")
                .email("email@test.com")
                .phone("+573000000000")
                .idDocument("123456789")
                .password("password123")
                .birthDate(LocalDate.now().minusYears(20))
                .role(null)
                .build();

        when(userPersistencePort.existsByIdDocument("123456789")).thenReturn(true);

        assertThrows(IdDocAlreadyExistsException.class, () -> {
            userUseCase.saveUser(existingDocumentUser, null);
        });

    }

}