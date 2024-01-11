package com.alvonellos.bug.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@AllArgsConstructor
public class AppConfig {
  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}
