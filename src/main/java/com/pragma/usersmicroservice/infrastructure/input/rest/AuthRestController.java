package com.pragma.usersmicroservice.infrastructure.input.rest;

import com.pragma.usersmicroservice.application.dto.response.JwtResponse;
import com.pragma.usersmicroservice.application.dto.request.LoginRequest;
import com.pragma.usersmicroservice.application.dto.response.UserValidationResponse;
import com.pragma.usersmicroservice.application.handler.IAuthHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Authentication management endpoints")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final IAuthHandler authHandler;

    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequestDto) {
        return ResponseEntity.ok(authHandler.login(loginRequestDto));
    }

    @Operation(summary = "Validate token", description = "Validates a JWT token and returns user information")
    @ApiResponse(responseCode = "200", description = "Token is valid")
    @ApiResponse(responseCode = "401", description = "Invalid or expired token", content = @Content)
    @GetMapping("/validateToken")
    public ResponseEntity<UserValidationResponse> validateToken(@RequestHeader("Authorization") String token ) {
        return ResponseEntity.ok(authHandler.validateToken(token.substring(7)));
    }
}
