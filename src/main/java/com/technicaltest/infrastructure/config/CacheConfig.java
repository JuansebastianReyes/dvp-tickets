package com.technicaltest.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {
  @Bean
  public CacheManager cacheManager() {
    Caffeine<Object, Object> builder = Caffeine.newBuilder()
      .expireAfterWrite(Duration.ofMinutes(5))
      .maximumSize(10000);
    CaffeineCacheManager manager = new CaffeineCacheManager("ticketsByUser");
    manager.setCaffeine(builder);
    return manager;
  }
}
