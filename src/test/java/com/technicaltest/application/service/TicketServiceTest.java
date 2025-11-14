package com.technicaltest.application.service;

import com.technicaltest.domain.model.Ticket;
import com.technicaltest.domain.model.TicketStatus;
import com.technicaltest.domain.port.TicketRepositoryPort;
import com.technicaltest.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {
  @Mock
  TicketRepositoryPort repository;
  @Mock
  UserRepositoryPort userRepository;

  @Test
  void findAllByUsuarioIdUsesCache() {
    UUID uid = UUID.randomUUID();
    ConcurrentMapCacheManager cm = new ConcurrentMapCacheManager("ticketsByUser");
    when(repository.findByFilters(eq(uid), isNull())).thenReturn(List.of());
    TicketService service = new TicketService(repository, userRepository, cm);
    service.findAll(uid, null);
    service.findAll(uid, null);
    verify(repository, times(2)).findByFilters(eq(uid), isNull());
  }

  @Test
  void closeChangesStatusAndEvictsCache() {
    UUID tid = UUID.randomUUID();
    UUID uid = UUID.randomUUID();
    Ticket t = new Ticket(tid, "desc", uid, LocalDateTime.now(), LocalDateTime.now(), TicketStatus.ABIERTO);
    ConcurrentMapCacheManager cm = new ConcurrentMapCacheManager("ticketsByUser");
    cm.getCache("ticketsByUser").put(uid, List.of(t));
    when(repository.findById(tid)).thenReturn(Optional.of(t));
    when(repository.update(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));
    TicketService service = new TicketService(repository, userRepository, cm);
    Optional<Ticket> res = service.close(tid);
    assertTrue(res.isPresent());
    assertEquals(TicketStatus.CERRADO, res.get().getStatus());
    assertNull(cm.getCache("ticketsByUser").get(uid));
  }
}
