package com.example.backend.infra.controller.user;

import com.example.backend.application.user.UserService;
import com.example.backend.domain.Product;
import com.example.backend.domain.User;
import com.example.backend.infra.controller.product.ProductDto;
import com.example.backend.infra.controller.user.dto.UserResponseDto;
import com.example.backend.infra.controller.user.dto.UserUpdateDto;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(UserResponseDto::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping("me")
    public UserResponseDto getMe(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());
        return UserResponseDto.fromModel(user);
    }

    @PatchMapping
    public UserResponseDto updateUser(@RequestBody @Valid UserUpdateDto userDto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.updatePersonalData(userDto, userDetails.getUsername());
        return UserResponseDto.fromModel(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }


    // CRUD operations for FavoriteProduct

    @PostMapping("/favorite")
    public Set<Product> addFavoriteProduct(@RequestBody @Valid ProductDto productDto, @AuthenticationPrincipal UserDetails userDetails) {
        Product product = productDto.toDomain();
        return userService.addFavoriteProduct(product, userDetails.getUsername());
    }

    @DeleteMapping("/favorite/{id}")
    public void removeFavoriteProduct(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        userService.removeFavoriteProduct(id, userDetails.getUsername());
    }

    @GetMapping("/favorite-list")
    public Set<Product> getFavoriteProducts(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getFavoriteProducts(userDetails.getUsername());
    }

    @DeleteMapping("/favorite-list")
    public void removeAllFavoriteProducts(@AuthenticationPrincipal UserDetails userDetails) {
        userService.removeAllFavoriteProducts(userDetails.getUsername());
    }


}
