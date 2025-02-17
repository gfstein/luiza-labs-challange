package com.example.backend.application.user;

import com.example.backend.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserServiceRepository {

//    CRUD operations for User

    User saveUser(User user);

    Optional<User> getUser(UUID id);

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    User updatePersonalData(User user);

    void deleteUser(UUID id);

}
