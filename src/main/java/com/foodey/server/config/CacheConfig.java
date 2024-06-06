package com.foodey.server.config;

import com.foodey.server.interceptor.TwoLevelCacheInterceptor;
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
import org.springframework.cache.annotation.AnnotationCacheOperationSource;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.cache.interceptor.CacheOperationSource;
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
  // Implementing a cache manager for bucket4j
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
    impl.createCache(
        "rate-limit-filter", configuration); // the cache for dynamic rate limit configiration
    impl.createCache("rate-limit-buckets", configuration); // the cache for bucket4j
    return impl;
  }

  @Primary
  @Bean("caffeineCacheManager")
  public CacheManager caffeineCacheManager() {
    // SimpleCacheManager cacheManager = new SimpleCacheManager();
    CaffeineCacheManager cacheManager =
        new CaffeineCacheManager("product", "products", "shop", "shops");
    cacheManager.setCaffeineSpec(CaffeineSpec.parse("maximumSize=3000,expireAfterAccess=30000s"));
    return cacheManager;
  }

  // @Bean
  // public CacheManager caffeineCacheManager(CaffeineCache caffeineCache) {
  //   SimpleCacheManager manager = new SimpleCacheManager();
  //   manager.setCaches(Arrays.asList(caffeineCache));
  //   return manager;
  // }

  // @Bean
  //    public CaffeineCache caffeineCacheConfig() {
  //        return new CaffeineCache("customerCache", Caffeine.newBuilder()
  //          .expireAfterWrite(Duration.ofMinutes(1))
  //          .initialCapacity(1)
  //          .maximumSize(2000)
  //          .build());
  //    }

  @Bean
  public RedisCacheConfiguration redisCacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(5))
        .disableCachingNullValues()
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer()));
  }

  @Bean("redisCacheManager")
  public CacheManager redisCacheManager(
      RedisConnectionFactory connectionFactory, RedisCacheConfiguration cacheConfiguration) {
    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory)
        .withCacheConfiguration("product", cacheConfiguration)
        .build();
  }

  @Bean
  public CacheInterceptor cacheInterceptor(
      CacheManager caffeineCacheManager, CacheOperationSource cacheOperationSource) {
    CacheInterceptor interceptor = new TwoLevelCacheInterceptor(caffeineCacheManager);
    interceptor.setCacheOperationSources(cacheOperationSource);
    return interceptor;
  }

  @Bean
  public CacheOperationSource cacheOperationSource() {
    return new AnnotationCacheOperationSource();
  }
}
