package com.technicaltest.infrastructure.web;

import com.technicaltest.application.service.TicketService;
import com.technicaltest.application.service.UserService;
import com.technicaltest.domain.model.TicketStatus;
import com.technicaltest.infrastructure.web.dto.TicketDtos;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
@Tag(name = "Tickets")
public class TicketController {
  private final TicketService service;
  private final UserService userService;

  public TicketController(TicketService service, UserService userService) {
    this.service = service;
    this.userService = userService;
  }

  @Operation(summary = "Crear ticket")
  @PostMapping
  public ResponseEntity<TicketDtos.Response> create(@RequestBody @Valid TicketDtos.CreateRequest body) {
    if (userService.findById(body.getUsuarioId()).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return service.create(body.getDescripcion(), body.getUsuarioId())
      .map(t -> ResponseEntity.created(URI.create("/tickets/" + t.getId())).body(TicketDtos.Response.from(t)))
      .orElseGet(() -> ResponseEntity.badRequest().build());
  }

  @Operation(summary = "Obtener ticket por id")
  @GetMapping("/{id}")
  public ResponseEntity<TicketDtos.Response> get(@PathVariable("id") UUID id) {
    return service.findById(id)
      .map(t -> ResponseEntity.ok(TicketDtos.Response.from(t)))
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Listar tickets")
  @GetMapping
  public ResponseEntity<List<TicketDtos.Response>> list(@RequestParam(value = "usuarioId", required = false) java.util.UUID usuarioId,
                                                        @RequestParam(value = "status", required = false) com.technicaltest.domain.model.TicketStatus status) {
    var list = service.findAll(usuarioId, status).stream().map(TicketDtos.Response::from).toList();
    return ResponseEntity.ok(list);
  }

  @Operation(summary = "Actualizar ticket")
  @PutMapping("/{id}")
  public ResponseEntity<TicketDtos.Response> update(@PathVariable("id") UUID id, @RequestBody @Valid TicketDtos.UpdateRequest body) {
    if (service.findById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    if (userService.findById(body.getUsuarioId()).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return service.update(id, body.getDescripcion(), body.getUsuarioId(), body.getStatus())
      .map(t -> ResponseEntity.ok(TicketDtos.Response.from(t)))
      .orElseGet(() -> ResponseEntity.badRequest().build());
  }

  @Operation(summary = "Eliminar ticket")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
    boolean ok = service.delete(id);
    if (ok) return ResponseEntity.noContent().build();
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Cerrar ticket")
  @PostMapping("/{id}/close")
  public ResponseEntity<TicketDtos.Response> close(@PathVariable("id") UUID id) {
    var existing = service.findById(id);
    if (existing.isEmpty()) return ResponseEntity.notFound().build();
    if (existing.get().getStatus() == TicketStatus.CERRADO) return ResponseEntity.status(409).build();
    return service.close(id)
      .map(t -> ResponseEntity.ok(TicketDtos.Response.from(t)))
      .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
