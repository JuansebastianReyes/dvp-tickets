package com.technicaltest.application.service;

import com.technicaltest.domain.port.DatabaseHealthPort;
import org.springframework.stereotype.Service;

@Service
public class DatabaseHealthService {
  private final DatabaseHealthPort healthPort;

  public DatabaseHealthService(DatabaseHealthPort healthPort) {
    this.healthPort = healthPort;
  }

  public boolean isHealthy() {
    return healthPort.isHealthy();
  }
}
