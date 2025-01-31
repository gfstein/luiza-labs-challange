package com.example.backend.infra.gateways.product;

import com.example.backend.domain.Product;
import com.example.backend.infra.persistence.product.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityMapper {

    public ProductEntity toEntity(Product product) {
        return new ProductEntity(product.getId(), product.getTitle(), product.getDescription());
    }

    public Product toDomain(ProductEntity productEntity) {
        return Product.create(productEntity.getId(), productEntity.getTitle(), productEntity.getDescription());
    }

}
