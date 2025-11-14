package com.technicaltest.infrastructure.db;

import com.technicaltest.domain.model.User;
import com.technicaltest.domain.port.UserRepositoryPort;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcUserRepositoryAdapter implements UserRepositoryPort {
  private final JdbcTemplate jdbcTemplate;

  public JdbcUserRepositoryAdapter(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<User> mapper = new RowMapper<User>() {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new User(
        UUID.fromString(rs.getString("id")),
        rs.getString("nombres"),
        rs.getString("apellidos"),
        rs.getString("usuario"),
        rs.getString("password_hash"),
        rs.getTimestamp("fecha_creacion").toLocalDateTime(),
        rs.getTimestamp("fecha_actualizacion").toLocalDateTime()
      );
    }
  };

  @Override
  public User save(User user) {
    jdbcTemplate.update(
      "INSERT INTO users(id, nombres, apellidos, usuario, password_hash, fecha_creacion, fecha_actualizacion) VALUES (?,?,?,?,?,?,?)",
      user.getId().toString(), user.getNombres(), user.getApellidos(), user.getUsuario(), user.getPasswordHash(), user.getFechaCreacion(), user.getFechaActualizacion()
    );
    return user;
  }

  @Override
  public Optional<User> findById(UUID id) {
    try {
      User u = jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", mapper, id.toString());
      return Optional.ofNullable(u);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<User> findByUsuario(String usuario) {
    try {
      User u = jdbcTemplate.queryForObject("SELECT * FROM users WHERE usuario = ?", mapper, usuario);
      return Optional.ofNullable(u);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<User> findAll() {
    return jdbcTemplate.query("SELECT * FROM users ORDER BY fecha_creacion DESC", mapper);
  }

  @Override
  public User update(User user) {
    jdbcTemplate.update(
      "UPDATE users SET nombres = ?, apellidos = ?, usuario = ?, password_hash = ?, fecha_actualizacion = ? WHERE id = ?",
      user.getNombres(), user.getApellidos(), user.getUsuario(), user.getPasswordHash(), user.getFechaActualizacion(), user.getId().toString()
    );
    return user;
  }

  @Override
  public void deleteById(UUID id) {
    jdbcTemplate.update("DELETE FROM users WHERE id = ?", id.toString());
  }
}
