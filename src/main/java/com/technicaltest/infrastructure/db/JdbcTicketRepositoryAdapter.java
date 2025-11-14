package com.technicaltest.infrastructure.db;

import com.technicaltest.domain.model.Ticket;
import com.technicaltest.domain.model.TicketStatus;
import com.technicaltest.domain.port.TicketRepositoryPort;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcTicketRepositoryAdapter implements TicketRepositoryPort {
  private final JdbcTemplate jdbcTemplate;

  public JdbcTicketRepositoryAdapter(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Ticket> mapper = new RowMapper<Ticket>() {
    @Override
    public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Ticket(
        UUID.fromString(rs.getString("id")),
        rs.getString("descripcion"),
        UUID.fromString(rs.getString("usuario_id")),
        rs.getTimestamp("fecha_creacion").toLocalDateTime(),
        rs.getTimestamp("fecha_actualizacion").toLocalDateTime(),
        TicketStatus.valueOf(rs.getString("status"))
      );
    }
  };

  @Override
  public Ticket save(Ticket ticket) {
    jdbcTemplate.update(
      "INSERT INTO tickets(id, descripcion, usuario_id, fecha_creacion, fecha_actualizacion, status) VALUES (?,?,?,?,?,?)",
      ticket.getId().toString(), ticket.getDescripcion(), ticket.getUsuarioId().toString(), ticket.getFechaCreacion(), ticket.getFechaActualizacion(), ticket.getStatus().name()
    );
    return ticket;
  }

  @Override
  public Optional<Ticket> findById(UUID id) {
    try {
      Ticket t = jdbcTemplate.queryForObject("SELECT * FROM tickets WHERE id = ?", mapper, id.toString());
      return Optional.ofNullable(t);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<Ticket> findAll() {
    return jdbcTemplate.query("SELECT * FROM tickets ORDER BY fecha_creacion DESC", mapper);
  }

  @Override
  public List<Ticket> findByFilters(UUID usuarioId, TicketStatus status) {
    StringBuilder sql = new StringBuilder("SELECT * FROM tickets WHERE 1=1");
    java.util.List<Object> args = new java.util.ArrayList<>();
    if (usuarioId != null) {
      sql.append(" AND usuario_id = ?");
      args.add(usuarioId.toString());
    }
    if (status != null) {
      sql.append(" AND status = ?");
      args.add(status.name());
    }
    sql.append(" ORDER BY fecha_creacion DESC");
    return jdbcTemplate.query(sql.toString(), mapper, args.toArray());
  }

  @Override
  public Ticket update(Ticket ticket) {
    jdbcTemplate.update(
      "UPDATE tickets SET descripcion = ?, usuario_id = ?, status = ?, fecha_actualizacion = ? WHERE id = ?",
      ticket.getDescripcion(), ticket.getUsuarioId().toString(), ticket.getStatus().name(), ticket.getFechaActualizacion(), ticket.getId().toString()
    );
    return ticket;
  }

  @Override
  public void deleteById(UUID id) {
    jdbcTemplate.update("DELETE FROM tickets WHERE id = ?", id.toString());
  }
}
