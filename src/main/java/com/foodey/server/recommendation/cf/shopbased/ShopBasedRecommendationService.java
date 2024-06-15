package com.foodey.server.recommendation.cf.shopbased;

import com.foodey.server.shop.model.Shop;
import com.foodey.server.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ShopBasedRecommendationService {

  Slice<Shop> recommendShopsForUser(User user, Pageable pageable);

  Slice<Shop> recommendShopsForUser(
      User user, double longitude, double latitude, long maxDistanceKms, Pageable pageable);
}
