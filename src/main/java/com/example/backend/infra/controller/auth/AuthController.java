package com.example.backend.infra.controller.auth;

import com.example.backend.application.user.UserService;
import com.example.backend.domain.User;
import com.example.backend.exceptions.DuplicateException;
import com.example.backend.infra.controller.auth.dto.LoginRequest;
import com.example.backend.infra.controller.auth.dto.LoginResponse;
import com.example.backend.infra.controller.auth.dto.SignupRequest;
import com.example.backend.infra.controller.user.dto.UserResponseDto;
import com.example.backend.util.ErrorResponse;
import com.example.backend.util.JwtHelper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody @Valid SignupRequest requestDto, UriComponentsBuilder uriBuilder) {
        logger.info("Attempting to create user with email: {}", requestDto.email());
        try {
            User user = userService.createUser(requestDto);
            var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
            logger.info("User created successfully with email: {}", requestDto.email());
            return ResponseEntity.created(uri).body(UserResponseDto.fromModel(user));
        } catch (DuplicateException e) {
            logger.warn("Duplicate user detected with email: {}", requestDto.email());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(
                    HttpStatus.CONFLICT.value(),
                    HttpStatus.CONFLICT.getReasonPhrase(),
                    e.getMessage()
            ));
        } catch (Exception e) {
            logger.error("Error creating user with email: {}", requestDto.email(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    "An unexpected error occurred"
            ));
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Attempting to login user with email: {}", request.email());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
            String token = JwtHelper.generateToken(request.email());
            logger.info("User logged in successfully with email: {}", request.email());
            return ResponseEntity.ok(new LoginResponse(request.email(), token));
        } catch (BadCredentialsException e) {
            logger.warn("Invalid credentials for user with email: {}", request.email());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Error during login for user with email: {}", request.email(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}