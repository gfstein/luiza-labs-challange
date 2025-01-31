package com.example.backend.application.user;

import com.example.backend.application.product.ProductServiceRepository;
import com.example.backend.application.user.mapper.UserMapper;
import com.example.backend.domain.Product;
import com.example.backend.domain.User;
import com.example.backend.domain.encoder.UserPasswordEncoder;
import com.example.backend.exceptions.DomainException;
import com.example.backend.exceptions.DuplicateException;
import com.example.backend.infra.controller.auth.dto.SignupRequest;
import com.example.backend.infra.controller.user.dto.UserUpdateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserServiceRepository repository;
    private final ProductServiceRepository productRepository;
    private final UserPasswordEncoder passwordEncoder;
    private final ProductDataProvider productDataProvider;
    private final UserMapper userMapper;

    public UserService(UserServiceRepository repository, ProductServiceRepository productRepository, UserPasswordEncoder passwordEncoder, ProductDataProvider productDataProvider, UserMapper userMapper) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
        this.productDataProvider = productDataProvider;
        this.userMapper = userMapper;
    }

    // CRUD operations for User

    public User createUser(SignupRequest userDto) {
        logger.info("Attempting to create user with email: {}", userDto.email());
        repository.getUserByEmail(userDto.email()).ifPresent(u -> {
            logger.warn("User with email {} already exists", u.getEmail());
            throw new DuplicateException("User with email " + u.getEmail() + " already exists");
        });

        User user = User.create(userDto.name(), userDto.email(), userDto.password(), passwordEncoder);

        logger.info("User created successfully with email: {}", user.getEmail());
        return repository.saveUser(user);
    }

    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return repository.getAllUsers();
    }

    public User updatePersonalData(UUID id, UserUpdateDto dto) {
        logger.info("Updating personal data for user with id: {}", id);
        User user = repository.getUser(id).orElseThrow(() -> {
            logger.error("User with id {} not found", id);
            throw new DomainException("User not found");
        });

        user = userMapper.map(dto, user);
        logger.info("Personal data updated for user with id: {}", id);
        return repository.updatePersonalData(user);
    }

    public void deleteUser(UUID id) {
        logger.info("Deleting user with id: {}", id);
        repository.deleteUser(id);
        logger.info("User with id {} deleted successfully", id);
    }

    // CRUD operations for FavoriteProduct

    public Set<Product> addFavoriteProduct(Product product, String email) {
        logger.info("Adding favorite product with id {} for user with email: {}", product.getId(), email);
        Product productDB = productRepository.getProduct(product.getId())
                .orElseGet(() -> {
                    logger.info("Product with id {} not found, creating new product", product.getId());
                    return productRepository.createProduct(product);
                });
        User user = repository.getUserByEmail(email).orElseThrow(() -> {
            logger.error("User with email {} not found", email);
            throw new DomainException("User not found");
        });

        if (!productDataProvider.validateProduct(productDB.getId())) {
            logger.error("Product with id {} is not valid", product.getId());
            throw new DomainException("Product is not valid");
        }

        user.addFavoriteProduct(productDB);
        repository.saveUser(user);
        logger.info("Favorite product added successfully for user with email: {}", email);
        return user.getProducts();
    }

    public void removeFavoriteProduct(Long productId, String userEmail) {
        logger.info("Removing favorite product with id {} for user with email: {}", productId, userEmail);
        User user = repository.getUserByEmail(userEmail).orElseThrow(() -> {
            logger.error("User with email {} not found", userEmail);
            throw new DomainException("User not found");
        });
        Product product = productRepository.getProduct(productId).orElseThrow(() -> {
            logger.error("Favorite product with id {} not found", productId);
            throw new DomainException("Favorite product not found");
        });
        user.removeFavoriteProduct(product);
        repository.saveUser(user);
        logger.info("Favorite product removed successfully for user with email: {}", userEmail);
    }

    public Set<Product> getFavoriteProducts(String userEmail) {
        logger.info("Fetching favorite products for user with email: {}", userEmail);
        User user = repository.getUserByEmail(userEmail).orElseThrow(() -> {
            logger.error("User with email {} not found", userEmail);
            throw new DomainException("User not found");
        });
        return user.getProducts();
    }

    public void removeAllFavoriteProducts(String userEmail) {
        logger.info("Removing all favorite products for user with email: {}", userEmail);
        User user = repository.getUserByEmail(userEmail).orElseThrow(() -> {
            logger.error("User with email {} not found", userEmail);
            throw new DomainException("User not found");
        });
        user.removeAllFavoriteProducts();
        repository.saveUser(user);
        logger.info("All favorite products removed successfully for user with email: {}", userEmail);
    }
}