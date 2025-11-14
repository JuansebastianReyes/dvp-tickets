package com.technicaltest.infrastructure.db;

import com.technicaltest.domain.port.DatabaseHealthPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresHealthAdapter implements DatabaseHealthPort {
  private final JdbcTemplate jdbcTemplate;

  public PostgresHealthAdapter(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public boolean isHealthy() {
    try {
      Integer r = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
      return r != null && r == 1;
    } catch (Exception e) {
      return false;
    }
  }
}
