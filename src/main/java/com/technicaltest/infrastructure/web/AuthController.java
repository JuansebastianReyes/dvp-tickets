package com.technicaltest.infrastructure.web;

import com.technicaltest.application.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import com.technicaltest.domain.port.UserRepositoryPort;
import com.technicaltest.infrastructure.security.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthService authService;
  private final UserRepositoryPort userRepository;
  private final JwtProvider jwtProvider;
  private final boolean devEnabled;
  private final String devSecret;

  public AuthController(AuthService authService,
                        UserRepositoryPort userRepository,
                        JwtProvider jwtProvider,
                        @Value("${security.devToken.enabled:false}") boolean devEnabled,
                        @Value("${security.devToken.secret:}") String devSecret) {
    this.authService = authService;
    this.userRepository = userRepository;
    this.jwtProvider = jwtProvider;
    this.devEnabled = devEnabled;
    this.devSecret = devSecret;
  }

  public static class LoginRequest {
    @NotBlank
    public String usuario;
    @NotBlank
    public String contrasena;
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginRequest body) {
    return authService.login(body.usuario, body.contrasena)
      .map(token -> ResponseEntity.ok(Map.of(
        "token", token,
        "tokenType", "Bearer"
      )))
      .orElseGet(() -> ResponseEntity.status(401).build());
  }

  public static class DevTokenRequest {
    @NotBlank
    public String usuario;
  }

  @PostMapping("/dev-token")
  public ResponseEntity<Map<String, String>> devToken(@RequestHeader(name = "X-Dev-Secret", required = false) String secret,
                                                      @RequestBody @Valid DevTokenRequest body) {
    if (!devEnabled || devSecret == null || devSecret.isBlank() || secret == null || !devSecret.equals(secret)) {
      return ResponseEntity.status(403).build();
    }
    return userRepository.findByUsuario(body.usuario)
      .map(u -> ResponseEntity.ok(Map.of("token", jwtProvider.generateToken(u.getUsuario()), "tokenType", "Bearer")))
      .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
