package com.technicaltest.infrastructure.web;

import com.technicaltest.application.service.DatabaseHealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/db")
public class HealthController {
  private final DatabaseHealthService service;

  public HealthController(DatabaseHealthService service) {
    this.service = service;
  }

  @GetMapping("/health")
  public ResponseEntity<String> health() {
    boolean ok = service.isHealthy();
    if (ok) return ResponseEntity.ok("OK");
    return ResponseEntity.status(503).body("UNAVAILABLE");
  }
}
