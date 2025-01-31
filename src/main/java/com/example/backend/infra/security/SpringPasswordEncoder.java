package com.example.backend.infra.security;


import com.example.backend.domain.encoder.UserPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SpringPasswordEncoder implements UserPasswordEncoder {

    private final PasswordEncoder passwordEncoder;

    public SpringPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
