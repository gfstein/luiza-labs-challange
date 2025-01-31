package com.example.backend.infra.controller.user;

import com.example.backend.application.user.UserService;
import com.example.backend.domain.Product;
import com.example.backend.domain.User;
import com.example.backend.infra.controller.product.ProductDto;
import com.example.backend.infra.controller.user.dto.UserResponseDto;
import com.example.backend.infra.controller.user.dto.UserUpdateDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        logger.info("Fetching all users");
        List<UserResponseDto> users = userService.getAllUsers().stream()
                .map(UserResponseDto::fromModel)
                .collect(Collectors.toList());
        logger.info("Fetched {} users", users.size());
        return users;
    }

    @PatchMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable UUID id, @RequestBody @Valid UserUpdateDto userDto) {
        logger.info("Updating user with ID: {}", id);
        User user = userService.updatePersonalData(id, userDto);
        logger.info("User updated successfully: {}", user.getEmail());
        return UserResponseDto.fromModel(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        logger.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        logger.info("User deleted successfully: {}", id);
    }

    @PostMapping("/favorite")
    public Set<Product> addFavoriteProduct(@RequestBody @Valid ProductDto productDto, @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Adding favorite product for user: {}", userDetails.getUsername());
        Product product = productDto.toDomain();
        Set<Product> favoriteProducts = userService.addFavoriteProduct(product, userDetails.getUsername());
        logger.info("Favorite product added successfully for user: {}", userDetails.getUsername());
        return favoriteProducts;
    }

    @DeleteMapping("/favorite/{id}")
    public void removeFavoriteProduct(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Removing favorite product with ID: {} for user: {}", id, userDetails.getUsername());
        userService.removeFavoriteProduct(id, userDetails.getUsername());
        logger.info("Favorite product removed successfully for user: {}", userDetails.getUsername());
    }

    @GetMapping("/favorite-list")
    public Set<Product> getFavoriteProducts(@AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Fetching favorite products for user: {}", userDetails.getUsername());
        Set<Product> favoriteProducts = userService.getFavoriteProducts(userDetails.getUsername());
        logger.info("Fetched {} favorite products for user: {}", favoriteProducts.size(), userDetails.getUsername());
        return favoriteProducts;
    }

    @DeleteMapping("/favorite-list")
    public void removeAllFavoriteProducts(@AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Removing all favorite products for user: {}", userDetails.getUsername());
        userService.removeAllFavoriteProducts(userDetails.getUsername());
        logger.info("All favorite products removed successfully for user: {}", userDetails.getUsername());
    }
}