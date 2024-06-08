package com.foodey.server.timertask;

import com.foodey.server.evaluation.model.OrderEvaluation;
import com.foodey.server.evaluation.repository.OrderEvaluationRepository;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.service.ShopService;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RatingCalculator {

  @Value("${foodey.rating.shop.max_shop_per_calculation:10000}")
  private int MAX_CACULATION_SHOPS_PER_DAY;

  @Value("${foodey.rating.shop.duration_between_calculations_ms:172800000}")
  private long DURATION_BETWEEN_RATING_CALCULATIONS_MS;

  private final ShopService shopService;
  private final OrderEvaluationRepository orderEvaluationRepository;

  /**
   * Recalculates the ratings of shops at the end of the day for any shops that have not been
   * calculated for two days
   *
   * <p>TODO: Need to optimize the query to calculate the ratings of shops
   */
  @Scheduled(cron = "${foodey.rating.shop.cron_expression:0 0 1 * * ?}")
  public void calculateShopsRating() {
    Instant dayInPast =
        Instant.now().minus(Duration.ofMillis(DURATION_BETWEEN_RATING_CALCULATIONS_MS));

    List<Shop> shops = shopService.getShopsNotRatedSince(dayInPast, MAX_CACULATION_SHOPS_PER_DAY);

    if (shops.isEmpty()) {
      log.info(
          "Calculated ratings for 0 shops that have not been rated since {} - Viet Nam time: {}",
          dayInPast,
          ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))
              .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
      return;
    }

    Set<String> shopIds = shops.stream().map(Shop::getId).collect(Collectors.toSet());

    // mark all the order_evaluations as rated
    List<OrderEvaluation> orderEvaluations =
        orderEvaluationRepository.saveAll(
            orderEvaluationRepository.findByShopIdInAndRated(shopIds, false).stream()
                .peek(orderEvaluation -> orderEvaluation.setRated(true))
                .collect(Collectors.toList()));

    Map<String, Double> shopRatings =
        orderEvaluations.stream()
            .collect(
                Collectors.groupingBy(
                    OrderEvaluation::getShopId,
                    Collectors.averagingDouble(OrderEvaluation::getRating)));

    shops =
        shops.stream()
            // filter shops that have not been any evaluations
            .filter(shop -> shopRatings.containsKey(shop.getId()))
            .map(
                shop -> {
                  final double newRating =
                      shop.isRatingCaculatedAtLeastOneTime()
                          ? shopRatings.get(shop.getId())
                          : (shop.getRating() + shopRatings.get(shop.getId())) / 2;
                  shop.setRating(newRating);
                  return shop;
                })
            .collect(Collectors.toList());

    shopService.saveAll(shops);

    log.info(
        "Calculated ratings for {} shops that have not been rated since {} - Viet Nam time: {}",
        shops.size(),
        dayInPast,
        ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
  }
}
