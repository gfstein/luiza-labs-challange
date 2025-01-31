package com.example.backend.infra.controller.user.dto;

import com.example.backend.domain.User;
import com.example.backend.infra.controller.auth.dto.SignupRequest;
import com.example.backend.util.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public record UserUpdateDto(

        Optional<@Size(min = 3, max = 50) String> name,

        Optional<@Email String> email,

        @ValidPassword
        Optional<String> password

) {
    public static SignupRequest fromModel(User user) {
        return new SignupRequest(user.getName(), user.getEmail(), user.getPassword());
    }
}

