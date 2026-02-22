package com.pragma.usersmicroservice.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "Error response structure")
public class ErrorResponse {
    
    @Schema(description = "Error message", example = "Email already exists in BD")
    private String message;
    
    @Schema(description = "Timestamp when the error occurred")
    private LocalDateTime timestamp;
}
