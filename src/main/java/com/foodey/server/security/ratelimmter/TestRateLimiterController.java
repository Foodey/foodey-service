package com.foodey.server.security.ratelimmter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestRateLimiterController {

  private final RedisTemplate template;

  @GetMapping("/rate-limiter")
  public void testRateLimiter() {
    template.opsForValue().set("loda", "hello world");

    // Test Rate Limiter
  }
}
