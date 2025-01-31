package com.example.backend.infra.provider;

import com.example.backend.application.user.ProductDataProvider;
import com.example.backend.exceptions.NotFoundException;
import com.example.backend.infra.provider.dto.ExternalProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ExternalProductDataProvider implements ProductDataProvider {

    @Value("${external.api.products.url}")
    private String productsApiUrl;

    private final RestTemplate restTemplate;

    public ExternalProductDataProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean validateProduct(Long id) {
        log.info("Validating product with ID: {}", id);

        try {
            log.info("Fetching product data from external API: {}", productsApiUrl);
            ResponseEntity<ExternalProductDto[]> response = restTemplate.getForEntity(productsApiUrl, ExternalProductDto[].class);

            if (Objects.isNull(response.getBody())) {
                log.error("No product data found from external API {}: {}", productsApiUrl, response.getStatusCode());
            }

            List<ExternalProductDto> products = Arrays.asList(response.getBody());
            log.info("Received {} products from external API.", products.size());

            Optional<ExternalProductDto> product = products.stream()
                    .filter(p -> p.id().equals(id))
                    .findFirst();

            if (product.isPresent()) {
                log.info("Product with ID {} found.", id);
                return true;
            } else {
                log.warn("Product with ID {} not found.", id);
                return false;
            }

        } catch (Exception e) {
            log.error("Error while validating product with ID {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
}
