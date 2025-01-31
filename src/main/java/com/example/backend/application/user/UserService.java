package com.example.backend.application.user;

import com.example.backend.application.product.ProductServiceRepository;
import com.example.backend.domain.Product;
import com.example.backend.domain.User;
import com.example.backend.exceptions.DomainException;
import com.example.backend.exceptions.DuplicateException;
import com.example.backend.infra.controller.auth.dto.SignupRequest;
import com.example.backend.infra.controller.user.dto.UserUpdateDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserServiceRepository repository;
    private final ProductServiceRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserServiceRepository repository, ProductServiceRepository productRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    CRUD operations for User

    public User createUser(SignupRequest userDto) {
        repository.getUserByEmail(userDto.email()).ifPresent(u -> {
            throw new DuplicateException("User with email " + u.getEmail() + " already exists");
        });
        String hashedPassword = passwordEncoder.encode(userDto.password());
        User user = User.create(userDto.name(), userDto.email(), hashedPassword);
        user.setPassword(hashedPassword);
        return repository.saveUser(user);
    }

    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }

    public User updatePersonalData(UUID id, UserUpdateDto dto) {
        User user = repository.getUser(id).orElseThrow();
        dto.name().ifPresent(user::setName);
        dto.email().ifPresent(user::setEmail);
        dto.password().ifPresent(user::setPassword);
        return repository.updatePersonalData(user);
    }

    public void deleteUser(UUID id) {
        repository.deleteUser(id);
    }

//   CRUD operations for FavoriteProduct

    public Set<Product> addFavoriteProduct(Product product, String email) {
        Product productDB = productRepository.getProduct(product.getId())
                .orElseGet(() -> productRepository.createProduct(product));
        User user = repository.getUserByEmail(email).orElseThrow();
        user.addFavoriteProduct(productDB);
        repository.saveUser(user);
        return user.getProducts();
    }

    public void removeFavoriteProduct(Long productId, String userEmail) {
        User user = repository.getUserByEmail(userEmail).orElseThrow();
        Product product = productRepository.getProduct(productId).orElseThrow(() -> new DomainException("Favorite product not found"));
        user.removeFavoriteProduct(product);
        repository.saveUser(user);
    }

    public Set<Product> getFavoriteProducts(String userEmail) {
        User user = repository.getUserByEmail(userEmail).orElseThrow();
        return user.getProducts();
    }

    public void removeAllFavoriteProducts(String userEmail) {
        User user = repository.getUserByEmail(userEmail).orElseThrow();
        user.removeAllFavoriteProducts();
        repository.saveUser(user);
    }

}
