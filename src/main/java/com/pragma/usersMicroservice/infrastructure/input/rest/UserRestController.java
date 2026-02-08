package com.pragma.usersMicroservice.infrastructure.input.rest;

import com.pragma.usersMicroservice.application.dto.UserRegisterRequest;
import com.pragma.usersMicroservice.application.handler.IUserHandler;
import com.pragma.usersMicroservice.application.handler.UserHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/")
@RequiredArgsConstructor
public class UserRestController {
    private final IUserHandler userHandler;

    @PostMapping("/registerOwner")
    public ResponseEntity<Void> registerOwner(@Valid @RequestBody UserRegisterRequest userRegisterRequest){
        userHandler.createOwner(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
