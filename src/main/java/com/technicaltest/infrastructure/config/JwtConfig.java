package com.technicaltest.infrastructure.config;

import com.technicaltest.infrastructure.security.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
  @Bean
  public JwtProvider jwtProvider(
    @Value("${security.jwt.secret}") String secret,
    @Value("${security.jwt.expirationMinutes}") int expirationMinutes
  ) {
    return new JwtProvider(secret, expirationMinutes);
  }
}
