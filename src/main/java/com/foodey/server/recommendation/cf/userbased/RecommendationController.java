package com.foodey.server.recommendation.cf.userbased;

import com.foodey.server.annotation.CurrentUser;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommendation")
public class RecommendationController {

  private final RecommendationService recommendationService;

  // @GetMapping("/products")
  // public Slice<Product> recommendProductsForUser(
  //     @CurrentUser User user, @PageableDefault(page = 0, size = 12) Pageable pageable) {
  //   return recommendationService.recommendProductsForUser(user, pageable);
  // }

  @GetMapping("/shops")
  public Slice<Shop> recommendShopsFofUser(
      @CurrentUser User user,
      @RequestParam(required = false) Double longitude,
      @RequestParam(required = false) Double latitude,
      @RequestParam(required = false, defaultValue = "5") long maxDistance,
      @PageableDefault(page = 0, size = 12) Pageable pageable) {
    if (longitude != null && latitude != null) {
      return recommendationService.recommendShopsForUser(
          user, longitude, latitude, maxDistance, pageable);
    }
    return recommendationService.recommendShopsForUser(user, pageable);
  }
}
