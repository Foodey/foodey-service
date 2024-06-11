package com.foodey.server.recommendation;

import com.foodey.server.common.repos.CacheRepository;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

/** UserShopRecommendedCache */
@Repository
public class UserShopRecommendedCache implements CacheRepository<String, List<String>> {

  private StringRedisTemplate redisTemplate;
  private ListOperations<String, String> listOperations;

  private static String PREFIX = "shop_recommeded:user:";
  private static Duration TTL = Duration.ofDays(1);

  public UserShopRecommendedCache(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.listOperations = redisTemplate.opsForList();
  }

  public void removeAllKeys() {
    redisTemplate.delete(redisTemplate.keys(PREFIX + "*"));
  }

  @Override
  public void put(String userId, List<String> shopIds, Duration ttl) {
    String key = PREFIX + userId;
    listOperations.rightPushAll(key, shopIds);
    if (ttl == null) ttl = TTL;
    redisTemplate.expire(key, ttl);
  }

  @Override
  public Optional<List<String>> get(String userId) {
    String key = PREFIX + userId;
    if (!redisTemplate.hasKey(key)) return Optional.empty();
    return Optional.ofNullable(listOperations.range(key, 0, -1));
  }

  @Override
  public Boolean remove(String userId) {
    return redisTemplate.delete(PREFIX + userId);
  }
}
