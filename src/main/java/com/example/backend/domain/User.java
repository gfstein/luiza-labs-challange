package com.example.backend.domain;

import com.example.backend.exceptions.DomainException;
import com.example.backend.util.Validator;
import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
public class User {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private final Set<Product> products = new HashSet<>();

    private User () {}

    public static User create(String name, String email, String password) {
        return create(UUID.randomUUID(), name, email, password);
    }

    public static User create(UUID id, String name, String email, String password) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }

    public void addFavoriteProduct(Product product) {
        if (product == null) {
            throw new DomainException("Favorite product cannot be null");
        }

        if (products.contains(product)) {
            throw new DomainException("Favorite product already exists");
        }

        if (products.size() >= 5) {
            throw new DomainException("Favorite products limit reached");
        }

        products.add(product);
    }

    public void removeFavoriteProduct(Product product) {
        if (product == null) {
            throw new DomainException("Favorite product cannot be null");
        }

        if (!products.contains(product)) {
            throw new DomainException("Favorite product does not exist");
        }

        products.remove(product);
    }

    public void addFavoriteList(Set<Product> products) {
        if (products == null) {
            throw new DomainException("Favorite products cannot be null");
        }

        if (products.size() > 5) {
            throw new DomainException("Favorite products limit reached");
        }

        this.products.addAll(products);
    }

    public void removeAllFavoriteProducts() {
        products.clear();
    }

    private void setId(UUID id) {
        if (id == null) {
            throw new DomainException("Id cannot be null");
        }

        this.id = id;
    }

    public void setName(String name) {
        if (Validator.isNullOrEmpty(name) || name.length() < 2) {
            throw new DomainException("Name must be at least 2 characters long");
        }

        this.name = name;
    }

    public void setEmail(String email) {

        if (Validator.isNullOrEmpty(email) || !Validator.isEmail(email)) {
            throw new DomainException("Email is invalid");
        }

        this.email = email;
    }

    public void setPassword(String password) {

        if (!Validator.isValidPassword(password)) {
            throw new DomainException("Password not strong enough");
        }

        this.password = password;
    }

    public void setProducts(Set<Product> products) {
        if (products == null) {
            throw new DomainException("Favorite products cannot be null");
        }

        if (products.size() > 5) {
            throw new DomainException("Favorite products limit reached");
        }

        this.products.addAll(products);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail());
    }
}
