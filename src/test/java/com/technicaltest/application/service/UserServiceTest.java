package com.technicaltest.application.service;

import com.technicaltest.domain.model.User;
import com.technicaltest.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock
  UserRepositoryPort repository;

  PasswordEncoder encoder = new BCryptPasswordEncoder();

  @Test
  void createEncodesPasswordAndSaves() {
    when(repository.findByUsuario("alopez")).thenReturn(java.util.Optional.empty());
    when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
    UserService service = new UserService(repository, encoder);
    User u = service.create("Ana", "Lopez", "alopez", "Secreta123!");
    assertNotNull(u.getId());
    assertNotEquals("Secreta123!", u.getPasswordHash());
    verify(repository, times(1)).save(any(User.class));
  }
}
