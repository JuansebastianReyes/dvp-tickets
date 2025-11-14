package com.technicaltest.application.service;

import com.technicaltest.domain.model.User;
import com.technicaltest.domain.port.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
  private final UserRepositoryPort repository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepositoryPort repository, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }

  public User create(String nombres, String apellidos, String usuario, String contrasena) {
    String hash = passwordEncoder.encode(contrasena);
    LocalDateTime now = LocalDateTime.now();
    User u = new User(UUID.randomUUID(), nombres, apellidos, usuario, hash, now, now);
    return repository.save(u);
  }

  public Optional<User> findById(UUID id) {
    return repository.findById(id);
  }

  public List<User> findAll() {
    return repository.findAll();
  }

  public Optional<User> update(UUID id, String nombres, String apellidos, String usuario, String contrasena) {
    Optional<User> existing = repository.findById(id);
    if (existing.isEmpty()) return Optional.empty();
    String hash = passwordEncoder.encode(contrasena);
    User updated = existing.get().withUpdated(nombres, apellidos, usuario, hash, LocalDateTime.now());
    return Optional.of(repository.update(updated));
  }

  public boolean delete(UUID id) {
    Optional<User> existing = repository.findById(id);
    if (existing.isEmpty()) return false;
    repository.deleteById(id);
    return true;
  }
}
