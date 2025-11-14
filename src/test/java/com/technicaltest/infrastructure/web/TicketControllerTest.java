package com.technicaltest.infrastructure.web;

import com.technicaltest.application.service.TicketService;
import com.technicaltest.domain.model.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.technicaltest.application.service.UserService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketControllerTest {
  private MockMvc mockMvc;
  private TicketService service;
  private UserService userService;

  @BeforeEach
  void setup() {
    service = Mockito.mock(TicketService.class);
    userService = Mockito.mock(UserService.class);
    TicketController controller = new TicketController(service, userService);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void listWithFiltersCallsService() throws Exception {
    UUID uid = UUID.randomUUID();
    Mockito.when(service.findAll(uid, TicketStatus.ABIERTO)).thenReturn(List.of());
    mockMvc.perform(get("/tickets").param("usuarioId", uid.toString()).param("status", "ABIERTO"))
      .andExpect(status().isOk());
    Mockito.verify(service, Mockito.times(1)).findAll(uid, TicketStatus.ABIERTO);
  }
}
