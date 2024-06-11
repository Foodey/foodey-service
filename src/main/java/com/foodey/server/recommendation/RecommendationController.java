package com.foodey.server.recommendation;

import com.foodey.server.annotation.CurrentUser;
import com.foodey.server.product.model.Product;
import com.foodey.server.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommendation")
public class RecommendationController {

  private final RecommendationService recommendationService;

  @GetMapping("/products")
  public Slice<Product> getRecommendedProducts(
      @CurrentUser User user,
      @PageableDefault(page = 0, size = 12, sort = "name", direction = Direction.ASC)
          Pageable pageable) {
    return recommendationService.recommendProductsForUser(user.getId(), pageable);
  }
}
