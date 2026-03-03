package com.pragma.usersmicroservice.application.handler.impl;

import com.pragma.usersmicroservice.application.dto.response.JwtResponse;
import com.pragma.usersmicroservice.application.dto.request.UserRegisterRequest;
import com.pragma.usersmicroservice.application.mapper.IUserRegisterRequestMapper;
import com.pragma.usersmicroservice.domain.api.IAuthServicePort;
import com.pragma.usersmicroservice.domain.api.IUserServicePort;
import com.pragma.usersmicroservice.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHandlerImplTest {

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private IAuthServicePort authServicePort;

    @Mock
    private IUserRegisterRequestMapper userRegisterRequestMapper;

    @InjectMocks
    private UserHandlerImpl userHandler;


    @Test
    @DisplayName("CreateOwner: Should map request and call user service")
    void createOwner_shouldCallService_whenRequestIsValid() {
        // Arrange
        UserRegisterRequest request = buildRequest();

        User mappedUser = buildValidUser("1");

        when(userRegisterRequestMapper.toUser(request)).thenReturn(mappedUser);

        // Act
        userHandler.createOwner(request);

        // Assert
        verify(userRegisterRequestMapper).toUser(request);
        verify(userServicePort).createOwner(mappedUser);
        verifyNoMoreInteractions(userServicePort);
    }

    @Test
    @DisplayName("CreateEmployee: Should map request and call user service")
    void createEmployee_shouldCallService_whenRequestIsValid() {
        // Arrange
        UserRegisterRequest request = buildRequest();
        User mappedUser = buildValidUser("2");

        when(userRegisterRequestMapper.toUser(request)).thenReturn(mappedUser);

        // Act
        userHandler.createEmployee(request);

        // Assert
        verify(userRegisterRequestMapper).toUser(request);
        verify(userServicePort).createEmployee(mappedUser);
        verifyNoMoreInteractions(userServicePort);
    }

    @Test
    @DisplayName("CreateCustomer: Should create user, login and return JWT token")
    void createCustomer_shouldReturnJwt_whenProcessIsSuccessful() {
        // Arrange
        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        UserRegisterRequest request = buildRequest();

        User mappedUser = buildValidUser("3");

        when(userRegisterRequestMapper.toUser(request)).thenReturn(mappedUser);
        when(authServicePort.login(request.getEmail(), request.getPassword())).thenReturn(expectedToken);

        // Act
        JwtResponse response = userHandler.createCustomer(request);

        // Assert
        verify(userRegisterRequestMapper).toUser(request);
        verify(userServicePort).createCustomer(mappedUser);
        verify(authServicePort).login(request.getEmail(), request.getPassword());

        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());
    }

    // --- Helper Methods ---


    private User buildValidUser(String id) {
        return User.builder()
                .id(id)
                .name("Test User")
                .lastName("Lastname")
                .email("valid.email@test.com")
                .phone("+573001234567")
                .idDocument("1000200030")
                .password("securePassword")
                .build();
    }

    private UserRegisterRequest buildRequest() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setName("Test Name");
        request.setLastName("Test Last");
        request.setEmail("test@mail.com");
        request.setPassword("1234");
        request.setPhone("+573001234567");
        request.setIdDocument("123456789");
        return request;
    }
}