package com.foodey.server.recommendation;

import com.foodey.server.product.model.Product;
import com.foodey.server.shop.model.Shop;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface RecommendationService {

  Slice<Product> recommendProductsForUser(String userId, Pageable pageable);

  Slice<Shop> recommendShopsForUser(String userId, Pageable pageable);

  Slice<Shop> recommendShopsForUser(
      String userId, double longitude, double latitude, long maxDistanceKms, Pageable pageable);
}
