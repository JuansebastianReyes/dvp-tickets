package com.technicaltest.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.technicaltest.domain.model.Ticket;
import com.technicaltest.domain.model.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public class TicketDtos {
  public static class CreateRequest {
    @NotBlank @Size(max = 500)
    private String descripcion;
    @NotNull
    @JsonProperty("usuarioId")
    private UUID usuarioId;

    public CreateRequest() {}
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }
  }

  public static class UpdateRequest {
    @NotBlank @Size(max = 500)
    private String descripcion;
    @NotNull
    @JsonProperty("usuarioId")
    private UUID usuarioId;
    @NotNull
    private TicketStatus status;

    public UpdateRequest() {}
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }
    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }
  }

  public static class Response {
    private UUID id;
    private String descripcion;
    private UUID usuarioId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private TicketStatus status;

    public Response(UUID id, String descripcion, UUID usuarioId, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion, TicketStatus status) {
      this.id = id;
      this.descripcion = descripcion;
      this.usuarioId = usuarioId;
      this.fechaCreacion = fechaCreacion;
      this.fechaActualizacion = fechaActualizacion;
      this.status = status;
    }

    public static Response from(Ticket t) {
      return new Response(t.getId(), t.getDescripcion(), t.getUsuarioId(), t.getFechaCreacion(), t.getFechaActualizacion(), t.getStatus());
    }

    public UUID getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public UUID getUsuarioId() { return usuarioId; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public TicketStatus getStatus() { return status; }
  }
}
