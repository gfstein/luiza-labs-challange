package com.example.backend.infra.controller.product;

import com.example.backend.domain.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductDto(
        Long id,

        @NotBlank
        @Size(min = 3, max = 250)
        String title,

        @NotBlank
        @Size(min = 3, max = 500)
        String description
) {

    public static ProductDto fromDomain(Product product) {
        return new ProductDto(product.getId(), product.getTitle(), product.getDescription());
    }

    public Product toDomain() {
        return Product.create(id, title, description);
    }
}
