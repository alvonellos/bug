package com.alvonellos.bug.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;

@Configuration
@AllArgsConstructor
public class AppConfig {
  @Bean
  public RestTemplate restTemplate() {
    final RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
    return restTemplate;
  }
  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}
