package com.technicaltest.application.service;

import com.technicaltest.domain.model.User;
import com.technicaltest.domain.port.UserRepositoryPort;
import com.technicaltest.infrastructure.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
  @Mock
  UserRepositoryPort userRepository;

  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  JwtProvider jwtProvider = new JwtProvider("ChangeThisJWTSecret_DevOnly_0123456789abcdefABCDEF!@#%_extra_length_secure", 60);

  @Test
  void loginSuccessReturnsToken() {
    String raw = "Secreta123!";
    String hash = passwordEncoder.encode(raw);
    User user = new User(UUID.randomUUID(), "Ana", "Lopez", "alopez", hash, LocalDateTime.now(), LocalDateTime.now());
    when(userRepository.findByUsuario("alopez")).thenReturn(Optional.of(user));
    AuthService service = new AuthService(userRepository, passwordEncoder, jwtProvider);
    Optional<String> token = service.login("alopez", raw);
    assertTrue(token.isPresent());
    assertFalse(token.get().isBlank());
  }

  @Test
  void loginFailsWithWrongPassword() {
    String hash = passwordEncoder.encode("Secreta123!");
    User user = new User(UUID.randomUUID(), "Ana", "Lopez", "alopez", hash, LocalDateTime.now(), LocalDateTime.now());
    when(userRepository.findByUsuario("alopez")).thenReturn(Optional.of(user));
    AuthService service = new AuthService(userRepository, passwordEncoder, jwtProvider);
    Optional<String> token = service.login("alopez", "otra");
    assertTrue(token.isEmpty());
  }
}
