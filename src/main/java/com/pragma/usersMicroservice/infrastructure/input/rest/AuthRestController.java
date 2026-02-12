package com.pragma.usersMicroservice.infrastructure.input.rest;

import com.pragma.usersMicroservice.application.dto.JwtResponse;
import com.pragma.usersMicroservice.application.dto.LoginRequest;
import com.pragma.usersMicroservice.application.dto.UserValidationResponse;
import com.pragma.usersMicroservice.application.handler.IAuthHandler;
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
    public ResponseEntity<UserValidationResponse> validateToken(@RequestParam String token) {
        return ResponseEntity.ok(authHandler.validateToken(token));
    }
}
