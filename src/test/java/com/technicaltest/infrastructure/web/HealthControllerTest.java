package com.technicaltest.infrastructure.web;

import com.technicaltest.application.service.DatabaseHealthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HealthControllerTest {
  private MockMvc mockMvc;
  private DatabaseHealthService service;

  @BeforeEach
  void setup() {
    service = Mockito.mock(DatabaseHealthService.class);
    HealthController controller = new HealthController(service);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void okWhenHealthy() throws Exception {
    Mockito.when(service.isHealthy()).thenReturn(true);
    mockMvc.perform(get("/db/health")).andExpect(status().isOk());
  }

  @Test
  void serviceUnavailableWhenNotHealthy() throws Exception {
    Mockito.when(service.isHealthy()).thenReturn(false);
    mockMvc.perform(get("/db/health")).andExpect(status().isServiceUnavailable());
  }
}
