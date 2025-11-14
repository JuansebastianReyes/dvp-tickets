package com.technicaltest.infrastructure.web;

import com.technicaltest.application.service.UserService;
import com.technicaltest.infrastructure.web.dto.UserDtos;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios")
public class UserController {
  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @Operation(summary = "Crear usuario")
  @PostMapping
  public ResponseEntity<UserDtos.Response> create(@RequestBody @Valid UserDtos.CreateRequest body) {
    var u = service.create(body.getNombres(), body.getApellidos(), body.getUsuario(), body.getContrasena());
    return ResponseEntity.created(URI.create("/users/" + u.getId())).body(UserDtos.Response.from(u));
  }

  @Operation(summary = "Obtener usuario por id")
  @GetMapping("/{id}")
  public ResponseEntity<UserDtos.Response> get(@PathVariable("id") UUID id) {
    return service.findById(id)
      .map(u -> ResponseEntity.ok(UserDtos.Response.from(u)))
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Listar usuarios")
  @GetMapping
  public ResponseEntity<List<UserDtos.Response>> list() {
    var list = service.findAll().stream().map(UserDtos.Response::from).toList();
    return ResponseEntity.ok(list);
  }

  @Operation(summary = "Actualizar usuario")
  @PutMapping("/{id}")
  public ResponseEntity<UserDtos.Response> update(@PathVariable("id") UUID id, @RequestBody @Valid UserDtos.UpdateRequest body) {
    return service.update(id, body.getNombres(), body.getApellidos(), body.getUsuario(), body.getContrasena())
      .map(u -> ResponseEntity.ok(UserDtos.Response.from(u)))
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Eliminar usuario")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
    boolean ok = service.delete(id);
    if (ok) return ResponseEntity.noContent().build();
    return ResponseEntity.notFound().build();
  }
}
