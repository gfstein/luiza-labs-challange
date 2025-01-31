/*
package com.example.backend.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    @Test
    void createUserWithValidData() {
        User user = User.create("John Doe", "john.doe@example.com", "StrongPass123!");
        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void createUserWithInvalidName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            User.create("J", "john.doe@example.com", "StrongPass123!");
        });
        assertEquals("Name must be at least 2 characters long", exception.getMessage());
    }

    @Test
    void createUserWithInvalidEmail() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            User.create("John Doe", "john.doeexample.com", "StrongPass123!");
        });
        assertEquals("Email must contain @", exception.getMessage());
    }

    @Test
    void createUserWithInvalidPassword() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            User.create("John Doe", "john.doe@example.com", "weak");
        });
        assertEquals("Password not strong enough", exception.getMessage());
    }

    @Test
    void addValidFavoriteProduct() {
        User user = User.create("John Doe", "john.doe@example.com", "StrongPass123!");
        Product product = Product.create("Product1", "Description1");
        user.addFavoriteProduct(product);
        assertTrue(user.getProducts().contains(product));
    }

    @Test
    void addNullFavoriteProduct() {
        User user = User.create("John Doe", "john.doe@example.com", "StrongPass123!");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.addFavoriteProduct(null);
        });
        assertEquals("Favorite product cannot be null", exception.getMessage());
    }

    @Test
    void addDuplicateFavoriteProduct() {
        User user = User.create("John Doe", "john.doe@example.com", "StrongPass123!");
        Product product = Product.create("Product1", "Description1");
        user.addFavoriteProduct(product);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.addFavoriteProduct(product);
        });
        assertEquals("Favorite product already exists", exception.getMessage());
    }

    @Test
    void addFavoriteProductLimitReached() {
        User user = User.create("John Doe", "john.doe@example.com", "StrongPass123!");
        for (int i = 1; i <= 5; i++) {
            user.addFavoriteProduct(Product.create("Product" + i, "Description" + i));
        }
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.addFavoriteProduct(Product.create("Product6", "Description6"));
        });
        assertEquals("Favorite products limit reached", exception.getMessage());
    }

}
*/
