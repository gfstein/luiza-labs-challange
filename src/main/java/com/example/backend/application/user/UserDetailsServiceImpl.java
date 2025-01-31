package com.example.backend.application.user;

import com.example.backend.domain.User;
import com.example.backend.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserServiceRepository repository;

    public UserDetailsServiceImpl(UserServiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        logger.info("Attempting to load user by email: {}", email);

        User user = repository.getUserByEmail(email).orElseThrow(() -> {
            logger.error("User not found with email: {}", email);
            return new NotFoundException(String.format("User does not exist, email: %s", email));
        });

        logger.info("User found: {}", user.getEmail());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}