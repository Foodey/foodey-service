package com.foodey.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BeanConfig {

  @Bean
  @Primary
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
