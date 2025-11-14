package com.technicaltest.infrastructure.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {
  @Test
  void generateAndValidate() {
    JwtProvider provider = new JwtProvider("ChangeThisJWTSecret_DevOnly_0123456789abcdefABCDEF!@#%_extra_length_secure", 1);
    String token = provider.generateToken("subject");
    assertNotNull(token);
    String subject = provider.validateAndGetSubject(token);
    assertEquals("subject", subject);
  }
}
