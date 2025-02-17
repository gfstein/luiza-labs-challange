package com.example.backend.infra.gateways.user;


import com.example.backend.domain.Product;
import com.example.backend.domain.User;
import com.example.backend.infra.persistence.product.ProductEntity;
import com.example.backend.infra.persistence.user.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserEntityMapper {

    public UserEntity toEntity(User user) {
        return new UserEntity(user.getId(), user.getName(), user.getEmail(), user.getPassword(),
                user.getProducts().stream()
                        .map(product -> new ProductEntity(product.getId(), product.getTitle(), product.getDescription()))
                        .collect(Collectors.toSet()));
    }

    public User toDomain(UserEntity userEntity) {
        User user = User.create(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userEntity.getPassword());
        user.addFavoriteList(
                userEntity.getProducts()
                        .stream()
                        .map(productEntity -> Product.create(
                                productEntity.getId(), productEntity.getTitle(), productEntity.getDescription()
                        ))
                        .collect(Collectors.toSet()));

        return user;
    }

}
