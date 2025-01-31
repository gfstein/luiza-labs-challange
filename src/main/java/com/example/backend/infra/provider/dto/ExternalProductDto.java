package com.example.backend.infra.provider.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalProductDto(
        Long id,
        String title,
        String description
) {
}