package com.technicaltest.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.technicaltest.domain.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserDtos {
  public static class CreateRequest {
    public CreateRequest() {}
    @NotBlank @Size(max = 150)
    private String nombres;
    @NotBlank @Size(max = 150)
    private String apellidos;
    @NotBlank @Size(max = 100)
    @JsonProperty("usuario")
    private String usuario;
    @NotBlank @Size(min = 8, max = 100)
    @JsonProperty("contrasena")
    private String contrasena;

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
  }

  public static class UpdateRequest {
    public UpdateRequest() {}
    @NotBlank @Size(max = 150)
    private String nombres;
    @NotBlank @Size(max = 150)
    private String apellidos;
    @NotBlank @Size(max = 100)
    @JsonProperty("usuario")
    private String usuario;
    @NotBlank @Size(min = 8, max = 100)
    @JsonProperty("contrasena")
    private String contrasena;

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
  }

  public static class Response {
    private UUID id;
    private String nombres;
    private String apellidos;
    private String usuario;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public Response(UUID id, String nombres, String apellidos, String usuario, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
      this.id = id;
      this.nombres = nombres;
      this.apellidos = apellidos;
      this.usuario = usuario;
      this.fechaCreacion = fechaCreacion;
      this.fechaActualizacion = fechaActualizacion;
    }

    public static Response from(User u) {
      return new Response(u.getId(), u.getNombres(), u.getApellidos(), u.getUsuario(), u.getFechaCreacion(), u.getFechaActualizacion());
    }

    public UUID getId() { return id; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getUsuario() { return usuario; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
  }
}
