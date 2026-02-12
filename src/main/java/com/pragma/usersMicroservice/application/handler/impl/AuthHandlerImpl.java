package com.pragma.usersMicroservice.application.handler.impl;

import com.pragma.usersMicroservice.application.dto.JwtResponse;
import com.pragma.usersMicroservice.application.dto.LoginRequest;
import com.pragma.usersMicroservice.application.handler.IAuthHandler;
import com.pragma.usersMicroservice.domain.api.IAuthServicePort;
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
}
