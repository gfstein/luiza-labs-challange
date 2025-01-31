package com.example.backend.application.product;

import com.example.backend.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductServiceRepository repository;

    public ProductService(ProductServiceRepository repository) {
        this.repository = repository;
    }

    public Product createProduct(Product product) {
        return repository.createProduct(product);
    }

    public Optional<Product> getProduct(Long id) {
        return repository.getProduct(id);
    }

    public Product updateProduct(Product product) {
        return repository.updateProduct(product);
    }

    public void deleteProduct(Long id) {
        repository.deleteProduct(id);
    }

    public List<Product> getAllProducts() {
        return repository.getAllProducts();
    }

}
