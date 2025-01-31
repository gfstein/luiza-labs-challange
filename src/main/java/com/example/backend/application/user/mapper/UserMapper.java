package com.example.backend.application.user.mapper;

import com.example.backend.domain.User;
import com.example.backend.infra.controller.user.dto.UserUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User map(final UserUpdateDto dto, User user) {

        dto.name().ifPresent(user::setName);
        dto.email().ifPresent(user::setEmail);
        dto.password().ifPresent(user::setPassword);

        return user;
    }
}
