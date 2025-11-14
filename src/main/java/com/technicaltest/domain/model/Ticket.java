package com.technicaltest.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
  private UUID id;
  private String descripcion;
  private UUID usuarioId;
  private LocalDateTime fechaCreacion;
  private LocalDateTime fechaActualizacion;
  private TicketStatus status;

  public Ticket(UUID id, String descripcion, UUID usuarioId, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion, TicketStatus status) {
    this.id = id;
    this.descripcion = descripcion;
    this.usuarioId = usuarioId;
    this.fechaCreacion = fechaCreacion;
    this.fechaActualizacion = fechaActualizacion;
    this.status = status;
  }

  public UUID getId() { return id; }
  public String getDescripcion() { return descripcion; }
  public UUID getUsuarioId() { return usuarioId; }
  public LocalDateTime getFechaCreacion() { return fechaCreacion; }
  public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
  public TicketStatus getStatus() { return status; }

  public Ticket withUpdated(String descripcion, UUID usuarioId, TicketStatus status, LocalDateTime fechaActualizacion) {
    return new Ticket(id, descripcion, usuarioId, fechaCreacion, fechaActualizacion, status);
  }
}
