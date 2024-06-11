package com.foodey.server.recommendation;

import com.foodey.server.product.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/** RecommendationService */
public interface RecommendationService {

  Slice<Product> recommendProductsForUser(String userId, Pageable pageable);
}
