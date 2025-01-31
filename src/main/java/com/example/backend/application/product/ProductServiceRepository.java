package com.example.backend.application.product;

import com.example.backend.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductServiceRepository {

    Product createProduct(Product product);

    Optional<Product> getProduct(Long id);

    Product updateProduct(Product product);

    void deleteProduct(Long id);

    List<Product> getAllProducts();

}
