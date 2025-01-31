package com.example.backend.infra.controller.auth.dto;

public record LoginResponse(
        String email,
        String token
) {
}
