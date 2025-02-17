package com.example.backend.infra.controller.product;

import com.example.backend.application.product.ProductService;
import com.example.backend.domain.Product;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto) {
        Product product = productService.createProduct(productDto.toDomain());
        return ProductDto.fromDomain(product);
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return products.stream().map(ProductDto::fromDomain).toList();
    }

    @PutMapping
    public ProductDto updateProduct(@RequestBody @Valid ProductDto productDto) {
        Product product = productService.updateProduct(productDto.toDomain());
        return ProductDto.fromDomain(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}
