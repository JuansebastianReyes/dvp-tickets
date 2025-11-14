package com.technicaltest.domain.port;

import com.technicaltest.domain.model.Ticket;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepositoryPort {
  Ticket save(Ticket ticket);
  Optional<Ticket> findById(UUID id);
  List<Ticket> findAll();
  List<Ticket> findByFilters(UUID usuarioId, com.technicaltest.domain.model.TicketStatus status);
  Ticket update(Ticket ticket);
  void deleteById(UUID id);
}
