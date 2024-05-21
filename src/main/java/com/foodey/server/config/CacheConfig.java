package com.foodey.server.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
  // @Bean
  // public CaffeineCache caffeineCacheConfig() {
  //   return new CaffeineCache(
  //       "rate-limit-buckets",
  //       Caffeine.newBuilder()
  //           .expireAfterWrite(Duration.ofMinutes(1))
  //           .initialCapacity(1)
  //           .maximumSize(2000)
  //           .build());
  // }

  // @Bean
  // public CacheManager caffeineCacheManager(CaffeineCache caffeineCache) {
  //   SimpleCacheManager manager = new SimpleCacheManager();
  //   manager.setCaches(Arrays.asList(caffeineCache));
  //   return manager;
  // }

  // @Bean
  // public RedisCacheConfiguration cacheConfiguration() {
  //   return RedisCacheConfiguration.defaultCacheConfig()
  //       .entryTtl(Duration.ofMinutes(5))
  //       .disableCachingNullValues()
  //       .serializeValuesWith(
  //           RedisSerializationContext.SerializationPair.fromSerializer(
  //               new GenericJackson2JsonRedisSerializer()));
  // }

  // @Bean
  // @Primary
  // public CacheManager redisCacheManager(
  //     RedisConnectionFactory connectionFactory, RedisCacheConfiguration cacheConfiguration) {
  //   return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory)
  //       .withCacheConfiguration("redis-redisson", cacheConfiguration)
  //       .build();
  // }
}
