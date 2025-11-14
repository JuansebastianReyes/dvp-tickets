package com.technicaltest.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
  private UUID id;
  private String nombres;
  private String apellidos;
  private String usuario;
  private String passwordHash;
  private LocalDateTime fechaCreacion;
  private LocalDateTime fechaActualizacion;

  public User(UUID id, String nombres, String apellidos, String usuario, String passwordHash, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
    this.id = id;
    this.nombres = nombres;
    this.apellidos = apellidos;
    this.usuario = usuario;
    this.passwordHash = passwordHash;
    this.fechaCreacion = fechaCreacion;
    this.fechaActualizacion = fechaActualizacion;
  }

  public UUID getId() { return id; }
  public String getNombres() { return nombres; }
  public String getApellidos() { return apellidos; }
  public String getUsuario() { return usuario; }
  public String getPasswordHash() { return passwordHash; }
  public LocalDateTime getFechaCreacion() { return fechaCreacion; }
  public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }

  public User withPasswordHash(String passwordHash) {
    return new User(id, nombres, apellidos, usuario, passwordHash, fechaCreacion, fechaActualizacion);
  }

  public User withUpdated(String nombres, String apellidos, String usuario, String passwordHash, LocalDateTime fechaActualizacion) {
    return new User(id, nombres, apellidos, usuario, passwordHash, fechaCreacion, fechaActualizacion);
  }
}
