package com.pragma.usersMicroservice.infrastructure.configuration;

import com.pragma.usersMicroservice.application.dto.UserValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "users-microservice", url = "${users.microservice.url}")
public interface UserFeignClient {
    UserValidationResponse validateToken(String token);
}
