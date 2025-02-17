package com.example.backend.infra.controller.auth.dto;

import java.util.UUID;

public record LoginResponse(
        UUID id,
        String email,
        String accessToken,
        String refreshToken
) {
}
