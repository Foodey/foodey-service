package com.foodey.server.timertask;

import com.foodey.server.recommendation.cf.userbased.RecommendationService;
import com.foodey.server.recommendation.cf.userbased.UserShopRecommendedCache;
import com.foodey.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationCaculator {

  private final RecommendationService recommendationService;
  private final UserShopRecommendedCache userShopRecommendedCache;
  private final UserRepository userRepository;

  /** pre caculate shops recommadation for all users based on collaborative filtering algorithm */
  @Scheduled(
      cron =
          "${foodey.recommendation.pre_compute.collaborative_filtering.shop_cron_expression:0 0 0 *"
              + " * ?}")
  public void caculateShopsRecommendationByCF() {

    log.info(
        "Start pre caculate shops recommadation for all users based on collaborative filtering"
            + " algorithm");

    userShopRecommendedCache.removeAllKeys();
    userRepository
        .findAll()
        .forEach(
            user -> {
              // get user id
              // compute shops recommendation for user and save it to cache
              recommendationService.recommendShopsForUser(user, PageRequest.of(0, 1));
            });

    log.info(
        "End pre caculate shops recommadation for all users based on collaborative filtering"
            + " algorithm");
  }
}
