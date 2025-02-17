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

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody @Valid SignupRequest requestDto, UriComponentsBuilder uriBuilder) {
        try {
            User user = userService.createUser(requestDto);
            var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
            return ResponseEntity.created(uri).body(UserResponseDto.fromModel(user));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(
                    HttpStatus.CONFLICT.value(),
                    HttpStatus.CONFLICT.getReasonPhrase(),
                    e.getMessage(
                    )));
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(createLoginResponse(request.email()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody String refreshToken) {
        try {
            String email = JwtHelper.validateRefreshToken(refreshToken);
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            return ResponseEntity.ok(createLoginResponse(email));
        } catch (Exception e) {
            // Em caso de token inválido ou expirado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private LoginResponse createLoginResponse(String email) {
        User user = userService.getUser(email);
        String accessToken = JwtHelper.generateToken(email);
        String refreshToken = JwtHelper.generateRefreshToken(email);
        return new LoginResponse(user.getId(), email, accessToken, refreshToken);
    }

}
