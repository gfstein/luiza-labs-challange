package com.example.backend.domain;

import com.example.backend.util.Validator;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Product {

    private Long id;
    private String title;
    private String description;

    private Product() {}

    public static Product create(Long id, String title, String description) {
        Product products = new Product();
        products.setId(id);
        products.setTitle(title);
        products.setDescription(description);
        return products;
    }

    private void setId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        this.id = id;
    }

    private void setTitle(String title) {
        if (Validator.isNullOrEmpty(title) || title.length() < 2) {
            throw new IllegalArgumentException("Title must be at least 2 characters long");
        }

        this.title = title;
    }

    private void setDescription(String description) {
        if (Validator.isNullOrEmpty(description) || description.length() < 2) {
            throw new IllegalArgumentException("Description must be at least 2 characters long");
        }

        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product product)) return false;
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
