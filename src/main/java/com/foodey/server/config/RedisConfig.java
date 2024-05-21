package com.foodey.server.config;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

  // @Value("spring.data.redis.host")
  // private String HOST;

  // @Value("spring.data.redis.port")
  // private String PORT;

  // @Value("spring.data.redis.password")
  // private String PASSWORD;

  @Bean
  public RedisConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
    return new RedissonConnectionFactory(redisson);
  }

  @Primary
  @Bean
  public RedisTemplate<Object, Object> redisTemplate(
      RedisConnectionFactory redissonConnectionFactory) {
    RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redissonConnectionFactory);
    return redisTemplate;
  }
}
