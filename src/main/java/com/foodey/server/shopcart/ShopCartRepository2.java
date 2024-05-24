// package com.foodey.server.shopcart;

// import java.time.Duration;
// import java.util.Optional;

// import com.foodey.server.common.repos.CacheRepository;

// import org.springframework.data.redis.core.HashOperations;
// import org.springframework.stereotype.Repository;

// /** ShopCartRepository */
// @Repository
// public class ShopCartRepository implements CacheRepository<String, ProductQuantity> {

//   private final RedisTemplate<String,String> reidsTemplate;
//   private final HashOperations hashOperations;

//   @Override
//   public void put(String key, ProductQuantity value, Duration ttl) {
//     // TODO Auto-generated method stub
//     throw new UnsupportedOperationException("Unimplemented method 'put'");
//   }

//   @Override
//   public Optional<ProductQuantity> get(String key) {
//     // TODO Auto-generated method stub
//     throw new UnsupportedOperationException("Unimplemented method 'get'");
//   }

//   @Override
//   public Boolean remove(String key) {
//     // TODO Auto-generated method stub
//     throw new UnsupportedOperationException("Unimplemented method 'remove'");
//   }

// }
