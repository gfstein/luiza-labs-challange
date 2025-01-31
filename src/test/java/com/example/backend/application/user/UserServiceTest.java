package com.example.backend.application.user;

import com.example.backend.application.product.ProductServiceRepository;
import com.example.backend.application.user.mapper.UserMapper;
import com.example.backend.domain.User;
import com.example.backend.domain.encoder.UserPasswordEncoder;
import com.example.backend.exceptions.DuplicateException;
import com.example.backend.infra.controller.auth.dto.SignupRequest;
import com.example.backend.infra.controller.user.dto.UserUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserServiceRepository repository;
    @Mock
    private ProductServiceRepository productRepository;
    @Mock
    private UserPasswordEncoder passwordEncoder;
    @Mock
    private ProductDataProvider productDataProvider;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    private SignupRequest signupRequest;
    private User user;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest("John Doe", "john.doe@example.com", "Password123!");
        user = User.create(signupRequest.name(), signupRequest.email(), "Password123!", passwordEncoder);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(repository.getUserByEmail(signupRequest.email())).thenReturn(Optional.empty());
        when(repository.saveUser(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(signupRequest);

        assertNotNull(createdUser);
        assertEquals(signupRequest.email(), createdUser.getEmail());
        verify(repository).saveUser(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        when(repository.getUserByEmail(signupRequest.email())).thenReturn(Optional.of(user));

        assertThrows(DuplicateException.class, () -> userService.createUser(signupRequest));
    }

    @Test
    void shouldGetAllUsers() {
        when(repository.getAllUsers()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }

    @Test
    void shouldUpdatePersonalDataSuccessfully() {
        UUID userId = UUID.randomUUID();
        UserUpdateDto updateDto = new UserUpdateDto(Optional.of("John Doe"), Optional.of("john.doe@example.com"), Optional.of("Password123!"));
        when(repository.getUser(userId)).thenReturn(Optional.of(user));
        when(userMapper.map(updateDto, user)).thenReturn(user);
        when(repository.updatePersonalData(user)).thenReturn(user);

        User updatedUser = userService.updatePersonalData(userId, updateDto);

        assertNotNull(updatedUser);
        verify(repository).updatePersonalData(user);
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        UUID userId = UUID.randomUUID();
        doNothing().when(repository).deleteUser(userId);

        assertDoesNotThrow(() -> userService.deleteUser(userId));
        verify(repository).deleteUser(userId);
    }
}
