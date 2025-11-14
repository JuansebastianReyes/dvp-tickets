package com.technicaltest.application.service;

import com.technicaltest.domain.port.UserRepositoryPort;
import com.technicaltest.infrastructure.security.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
  private final UserRepositoryPort userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  public AuthService(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtProvider = jwtProvider;
  }

  public Optional<String> login(String usuario, String contrasena) {
    var userOpt = userRepository.findByUsuario(usuario);
    if (userOpt.isEmpty()) return Optional.empty();
    var user = userOpt.get();
    if (!passwordEncoder.matches(contrasena, user.getPasswordHash())) return Optional.empty();
    String token = jwtProvider.generateToken(user.getUsuario());
    return Optional.of(token);
  }
}
