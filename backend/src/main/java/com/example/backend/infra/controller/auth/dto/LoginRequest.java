package com.example.backend.infra.controller.auth.dto;

import com.example.backend.util.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,

        @ValidPassword
        String password
) {
}
