package com.technicaltest.infrastructure.web;

import com.technicaltest.application.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
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
}
