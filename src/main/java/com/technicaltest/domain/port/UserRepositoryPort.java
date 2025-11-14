package com.technicaltest.domain.port;

import com.technicaltest.domain.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
  User save(User user);
  Optional<User> findById(UUID id);
  Optional<User> findByUsuario(String usuario);
  List<User> findAll();
  User update(User user);
  void deleteById(UUID id);
}
