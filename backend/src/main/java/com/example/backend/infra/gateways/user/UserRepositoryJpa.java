package com.example.backend.infra.gateways.user;

import com.example.backend.application.user.UserServiceRepository;
import com.example.backend.domain.User;
import com.example.backend.infra.persistence.user.UserEntity;
import com.example.backend.infra.persistence.user.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class UserRepositoryJpa implements UserServiceRepository {

    private final UserEntityMapper mapper;
    private final UserRepository repository;

    public UserRepositoryJpa(UserEntityMapper mapper, UserRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }


    @Override
    public User saveUser(User user) {
        UserEntity entity = mapper.toEntity(user);
        repository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<User> getUser(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public User updatePersonalData(User user) {
        UserEntity entity = repository.findById(user.getId()).orElseThrow();
        UserEntity updatedEntity = mapper.toEntity(user);
        updatedEntity.setId(entity.getId());
        UserEntity saved = repository.save(updatedEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteUser(UUID id) {
        repository.deleteById(id);
    }
}
