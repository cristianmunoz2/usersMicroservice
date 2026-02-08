package com.pragma.usersMicroservice.domain.usecase;

import com.pragma.usersMicroservice.domain.exception.EmailAlreadyExistsException;
import com.pragma.usersMicroservice.domain.exception.IdDocAlreadyExistsException;
import com.pragma.usersMicroservice.domain.exception.UnderAgeException;
import com.pragma.usersMicroservice.domain.model.User;
import com.pragma.usersMicroservice.domain.spi.IPasswordEncryptionPort;
import com.pragma.usersMicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersMicroservice.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

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