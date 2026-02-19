package com.pragma.usersmicroservice.infrastructure.input.rest;

import com.pragma.usersmicroservice.application.dto.response.JwtResponse;
import com.pragma.usersmicroservice.application.dto.request.LoginRequest;
import com.pragma.usersmicroservice.application.dto.response.UserValidationResponse;
import com.pragma.usersmicroservice.application.handler.IAuthHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final IAuthHandler authHandler;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequestDto) {
        return ResponseEntity.ok(authHandler.login(loginRequestDto));
    }

    @GetMapping("/validateToken")
    public ResponseEntity<UserValidationResponse> validateToken(@RequestHeader("Authorization") String token ) {
        return ResponseEntity.ok(authHandler.validateToken(token.substring(7)));
    }
}
