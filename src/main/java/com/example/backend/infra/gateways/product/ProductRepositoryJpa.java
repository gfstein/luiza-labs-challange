package com.example.backend.infra.gateways.product;

import com.example.backend.application.product.ProductServiceRepository;
import com.example.backend.domain.Product;
import com.example.backend.infra.persistence.product.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryJpa implements ProductServiceRepository {

    private final ProductEntityMapper mapper;
    private final ProductRepository repository;

    public ProductRepositoryJpa(ProductEntityMapper mapper, ProductRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public Product createProduct(Product product) {
        return mapper.toDomain(repository.save(mapper.toEntity(product)));
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Product updateProduct(Product product) {
        return mapper.toDomain(repository.save(mapper.toEntity(product)));
    }

    @Override
    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }
}
