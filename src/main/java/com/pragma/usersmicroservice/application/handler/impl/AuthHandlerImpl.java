package com.pragma.usersmicroservice.application.handler.impl;

import com.pragma.usersmicroservice.application.dto.response.JwtResponse;
import com.pragma.usersmicroservice.application.dto.request.LoginRequest;
import com.pragma.usersmicroservice.application.dto.response.UserValidationResponse;
import com.pragma.usersmicroservice.application.handler.IAuthHandler;
import com.pragma.usersmicroservice.domain.api.IAuthServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthHandlerImpl implements IAuthHandler {

    private final IAuthServicePort authServicePort;

    @Override
    public JwtResponse login(LoginRequest loginRequestDto) {
        String token = authServicePort.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        return new JwtResponse(token);
    }

    @Override
    public UserValidationResponse validateToken(String token) {
        return authServicePort.validateNewToken(token);
    }

}
