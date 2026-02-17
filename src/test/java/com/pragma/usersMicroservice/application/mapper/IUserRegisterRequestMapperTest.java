package com.pragma.usersMicroservice.application.mapper;

import com.pragma.usersMicroservice.application.dto.UserRegisterRequest;
import com.pragma.usersMicroservice.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class IUserRegisterRequestMapperTest {

    private final IUserRegisterRequestMapper mapper = Mappers.getMapper(IUserRegisterRequestMapper.class);

    @Test
    @DisplayName("Should map DTO to Entity correctly")
    void toUser_shouldMapFieldsCorrectly() {
        // Arrange
        UserRegisterRequest dto = new UserRegisterRequest();
        dto.setName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@test.com");
        dto.setPassword("1234");
        dto.setPhone("+573001234567");
        dto.setIdDocument("123456789");
        dto.setBirthDate(java.time.LocalDate.of(1990, 1, 1));

        // Act
        User user = mapper.toUser(dto);

        // Assert
        assertNotNull(user);
        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getLastName(), user.getLastName());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getPassword(), user.getPassword());
        assertEquals(dto.getPhone(), user.getPhone());
        assertEquals(dto.getIdDocument(), user.getIdDocument());
        assertEquals(dto.getBirthDate(), user.getBirthDate());
        assertNull(user.getId());
    }

    @Test
    @DisplayName("Should return null when input is null")
    void toUser_shouldReturnNull_whenInputIsNull() {
        User result = mapper.toUser(null);
        assertNull(result);
    }
}