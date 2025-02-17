package com.example.backend.util;

public record ErrorResponse(
        Integer status,
        String error,
        String message) {
}
