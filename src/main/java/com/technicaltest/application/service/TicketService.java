package com.technicaltest.application.service;

import com.technicaltest.domain.model.Ticket;
import com.technicaltest.domain.model.TicketStatus;
import com.technicaltest.domain.port.TicketRepositoryPort;
import com.technicaltest.domain.port.UserRepositoryPort;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {
  private final TicketRepositoryPort repository;
  private final UserRepositoryPort userRepository;
  private final CacheManager cacheManager;

  public TicketService(TicketRepositoryPort repository, UserRepositoryPort userRepository, CacheManager cacheManager) {
    this.repository = repository;
    this.userRepository = userRepository;
    this.cacheManager = cacheManager;
  }

  @CacheEvict(cacheNames = "ticketsByUser", key = "#usuarioId")
  public Optional<Ticket> create(String descripcion, UUID usuarioId) {
    if (userRepository.findById(usuarioId).isEmpty()) return Optional.empty();
    LocalDateTime now = LocalDateTime.now();
    Ticket t = new Ticket(UUID.randomUUID(), descripcion, usuarioId, now, now, TicketStatus.ABIERTO);
    return Optional.of(repository.save(t));
  }

  public Optional<Ticket> findById(UUID id) {
    return repository.findById(id);
  }

  public List<Ticket> findAll() {
    return repository.findAll();
  }

  @Cacheable(cacheNames = "ticketsByUser", key = "#usuarioId", condition = "#usuarioId != null && #status == null")
  public List<Ticket> findAll(java.util.UUID usuarioId, TicketStatus status) {
    if (usuarioId == null && status == null) {
      return repository.findAll();
    }
    return repository.findByFilters(usuarioId, status);
  }

  @CacheEvict(cacheNames = "ticketsByUser", key = "#usuarioId")
  public Optional<Ticket> update(UUID id, String descripcion, UUID usuarioId, TicketStatus status) {
    Optional<Ticket> existing = repository.findById(id);
    if (existing.isEmpty()) return Optional.empty();
    if (userRepository.findById(usuarioId).isEmpty()) return Optional.empty();
    Ticket updated = existing.get().withUpdated(descripcion, usuarioId, status, LocalDateTime.now());
    return Optional.of(repository.update(updated));
  }

  public boolean delete(UUID id) {
    Optional<Ticket> existing = repository.findById(id);
    if (existing.isEmpty()) return false;
    repository.deleteById(id);
    if (cacheManager.getCache("ticketsByUser") != null) {
      cacheManager.getCache("ticketsByUser").evict(existing.get().getUsuarioId());
    }
    return true;
  }

  public Optional<Ticket> close(UUID id) {
    Optional<Ticket> existing = repository.findById(id);
    if (existing.isEmpty()) return Optional.empty();
    Ticket t = existing.get();
    if (t.getStatus() == TicketStatus.CERRADO) return Optional.of(t);
    Ticket updated = t.withUpdated(t.getDescripcion(), t.getUsuarioId(), TicketStatus.CERRADO, LocalDateTime.now());
    Optional<Ticket> res = Optional.of(repository.update(updated));
    if (cacheManager.getCache("ticketsByUser") != null) {
      cacheManager.getCache("ticketsByUser").evict(t.getUsuarioId());
    }
    return res;
  }
}
