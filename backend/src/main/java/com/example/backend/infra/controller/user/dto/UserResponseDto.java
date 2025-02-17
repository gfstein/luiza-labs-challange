package com.example.backend.infra.controller.user.dto;

import com.example.backend.domain.User;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String name,
        String email
) {
    public static UserResponseDto fromModel(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
