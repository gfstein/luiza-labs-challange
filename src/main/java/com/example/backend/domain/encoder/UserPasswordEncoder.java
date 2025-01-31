package com.example.backend.domain.encoder;

public interface UserPasswordEncoder {
    String encode(String rawPassword);
}
