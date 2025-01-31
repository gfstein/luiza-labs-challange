package com.example.backend.infra.controller.auth.dto;

import com.example.backend.util.annotation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(

        @NotBlank
        @Size(min = 3, max = 50)
        String name,

        @Email
        String email,

        @ValidPassword
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password

) {
}
