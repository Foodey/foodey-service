package com.foodey.server.recommendation.cf.userbased;

import com.foodey.server.shop.model.Shop;
import com.foodey.server.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface RecommendationService {

  // Slice<Product> recommendProductsForUser(User userId, Pageable pageable);

  Slice<Shop> recommendShopsForUser(User userId, Pageable pageable);

  Slice<Shop> recommendShopsForUser(
      User userId, double longitude, double latitude, long maxDistanceKms, Pageable pageable);
}
