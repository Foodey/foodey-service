package com.foodey.server.config;

import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.github.benmanes.caffeine.jcache.CacheManagerImpl;
import com.github.benmanes.caffeine.jcache.configuration.CaffeineConfiguration;
import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;
import java.net.URI;
import java.time.Duration;
import java.util.OptionalLong;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
@ImportAutoConfiguration({
  CacheAutoConfiguration.class,
})
public class CacheConfig {

  // https://stackoverflow.com/questions/77973235/why-is-bucket4j-not-working-with-caffeine
  @Bean
  public javax.cache.CacheManager cacheManagerForBucket() {
    CacheManagerImpl impl =
        new CacheManagerImpl(
            new CaffeineCachingProvider(),
            false,
            URI.create("com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider"),
            Thread.currentThread().getContextClassLoader(),
            new Properties());
    CaffeineConfiguration<Object, Object> configuration =
        new CaffeineConfiguration<Object, Object>();
    configuration.setExpireAfterAccess(
        OptionalLong.of(TimeUnit.NANOSECONDS.convert(259200, TimeUnit.SECONDS)));
    impl.createCache("rate-limit-buckets", configuration);
    return impl;
  }

  @Bean
  @Primary
  public CacheManager caffeineCacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("caffe", "caffe2");
    cacheManager.setCaffeineSpec(CaffeineSpec.parse("maximumSize=2000,expireAfterAccess=30000s"));
    return cacheManager;
  }

  @Bean
  public RedisCacheConfiguration redisCacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(5))
        .disableCachingNullValues()
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer()));
  }

  @Bean
  public CacheManager redisCacheManager(
      RedisConnectionFactory connectionFactory, RedisCacheConfiguration cacheConfiguration) {
    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory)
        .withCacheConfiguration("productCache", cacheConfiguration)
        .build();
  }
}
