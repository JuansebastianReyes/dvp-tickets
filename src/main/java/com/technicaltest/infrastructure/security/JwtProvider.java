package com.technicaltest.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtProvider {
  private final SecretKey secretKey;
  private final int expirationMinutes;

  public JwtProvider(String secret, int expirationMinutes) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    this.expirationMinutes = expirationMinutes;
  }

  public String generateToken(String subject) {
    Instant now = Instant.now();
    return Jwts.builder()
      .setSubject(subject)
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
      .signWith(secretKey, SignatureAlgorithm.HS256)
      .compact();
  }

  public String validateAndGetSubject(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(secretKey)
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }
}
