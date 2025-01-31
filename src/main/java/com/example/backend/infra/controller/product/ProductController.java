package com.example.backend.infra.controller.product;

import com.example.backend.application.product.ProductService;
import com.example.backend.domain.Product;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto) {
        logger.info("Creating a new product: {}", productDto.title());
        Product product = productService.createProduct(productDto.toDomain());
        logger.info("Product created successfully with ID: {}", product.getId());
        return ProductDto.fromDomain(product);
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        logger.info("Fetching all products");
        List<Product> products = productService.getAllProducts();
        logger.info("Fetched {} products", products.size());
        return products.stream().map(ProductDto::fromDomain).toList();
    }

    @PutMapping
    public ProductDto updateProduct(@RequestBody @Valid ProductDto productDto) {
        logger.info("Updating product with ID: {}", productDto.id());
        Product product = productService.updateProduct(productDto.toDomain());
        logger.info("Product updated successfully: {}", product.getId());
        return ProductDto.fromDomain(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        logger.info("Deleting product with ID: {}", id);
        productService.deleteProduct(id);
        logger.info("Product deleted successfully: {}", id);
    }
}