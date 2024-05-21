package com.foodey.server.otp;

import com.foodey.server.common.repos.CacheRepository;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OTPRepository implements CacheRepository<String, String> {

  private final StringRedisTemplate redisTemplate;

  private static String OTP_PREFIX = "otp:";

  @Override
  public void put(String key, String value, Duration ttl) {
    redisTemplate.opsForValue().set(OTP_PREFIX + key, value, ttl);
  }

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(OTP_PREFIX + key));
  }

  @Override
  public Boolean remove(String key) {
    return redisTemplate.delete(OTP_PREFIX + key);
  }
}
