package com.foodey.server.recommendation.cf.userbased;

import com.foodey.server.evaluation.model.OrderEvaluation;
import com.foodey.server.evaluation.repository.OrderEvaluationRepository;
import com.foodey.server.evaluation.repository.ProductEvaluationRepository;
import com.foodey.server.product.repository.ProductRepository;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.service.ShopService;
import com.foodey.server.user.model.User;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class EvaluationBasedRecommendationServiceImpl
    implements EvaluationBasedRecommendationService {

  private final ProductEvaluationRepository productEvaluationRepository;
  private final OrderEvaluationRepository orderEvaluationRepository;
  private final ProductRepository productRepository;
  private final ShopService shopService;
  private final UserShopRecommendedCache userShopRecommendedCache;

  /**
   * Recommends products for a user based on their and others' product evaluations.
   *
   * @param userId the ID of the user for whom recommendations are being generated.
   * @param pageable the pagination information.
   * @return a slice of recommended products.
   */
  // @Override
  // public Slice<Product> recommendProductsForUser(User user, Pageable pageable) {
  //   String userId = user.getId();
  //   Map<String, Map<String, Double>> userProductMatrix = buildUserProductMatrix();
  //   Map<String, Double> currentUserProductRatings = userProductMatrix.get(userId);
  //   // if user has no ratings at the moment

  //   Map<String, Double> similarityScores =
  //       calculateSimilarityScores(userProductMatrix, currentUserProductRatings, userId);
  //   List<String> recommendedProductIds =
  //       generateRecommendations(similarityScores, userProductMatrix,
  // currentUserProductRatings);

  //   return productRepository.findByIdIn(recommendedProductIds, pageable);
  // }

  private List<String> readCacheOrFetchRecommendations(User user) {
    String userId = user.getId();
    return userShopRecommendedCache
        .get(userId)
        .orElseGet(
            () -> {
              Map<String, Map<String, Double>> userShopMatrix = buildUserShopMatrix();
              Map<String, Double> currentUserShopRatings = userShopMatrix.get(userId);

              // if user has no ratings at the moment
              if (currentUserShopRatings == null) return new ArrayList<>();

              Map<String, Double> similarityScores =
                  calculateSimilarityScores(userShopMatrix, currentUserShopRatings, userId);
              List<String> newRecommendedShopIds =
                  generateRecommendations(similarityScores, userShopMatrix, currentUserShopRatings);
              userShopRecommendedCache.put(userId, newRecommendedShopIds, Duration.ofDays(1));

              log.info("Recommended shops for user {}: {}", userId, newRecommendedShopIds);
              return newRecommendedShopIds;
            });
  }

  /**
   * Recommends shops for a user based on their and others' shop evaluations.
   *
   * @param userId the ID of the user for whom recommendations are being generated.
   * @param pageable the pagination information.
   * @return a slice of recommended shops.
   */
  @Override
  public Slice<Shop> recommendShopsForUser(User user, Pageable pageable) {
    List<String> recommendedShopIds = readCacheOrFetchRecommendations(user);
    if (recommendedShopIds == null || recommendedShopIds.isEmpty()) {
      return shopService.findAll(pageable);
    }
    return shopService.findAllById(recommendedShopIds, pageable);
  }

  @Override
  public Slice<Shop> recommendShopsForUser(
      User user, double longitude, double latitude, long maxDistanceKms, Pageable pageable) {
    List<String> recommendedShopIds = readCacheOrFetchRecommendations(user);

    if (recommendedShopIds == null || recommendedShopIds.isEmpty()) {
      return shopService.findAllNear(longitude, latitude, maxDistanceKms, pageable);
    }

    return shopService.findAllByIdNear(
        recommendedShopIds, longitude, latitude, maxDistanceKms, pageable);
  }

  /**
   * Builds the user-product evaluation matrix.
   *
   * @return a map where the key is the user ID and the value is another map with product IDs and
   *     their ratings.
   */
  // private Map<String, Map<String, Double>> buildUserProductMatrix() {
  //   final List<ProductEvaluation> pEvaluations = productEvaluationRepository.findAll();
  //   final Map<String, Map<String, Double>> userProductMatrix = new HashMap<>();

  //   pEvaluations.forEach(
  //       evaluation -> {
  //         String creatorId = evaluation.getCreatorId();
  //         String productId = evaluation.getProductId();
  //         Map<String, Double> productRatings =
  //             userProductMatrix.computeIfAbsent(creatorId, k -> new HashMap<>());

  //         productRatings.merge(
  //             productId,
  //             (double) evaluation.getRating(),
  //             (oldRating, newRating) -> (newRating + oldRating) / 2);
  //       });

  //   return userProductMatrix;
  // }

  /**
   * Builds the user-shop evaluation matrix.
   *
   * @return a map where the key is the user ID and the value is another map with shop IDs and their
   *     ratings.
   */
  private Map<String, Map<String, Double>> buildUserShopMatrix() {
    final List<OrderEvaluation> oEvaluations = orderEvaluationRepository.findAll();
    final Map<String, Map<String, Double>> userShopMatrix = new HashMap<>();

    oEvaluations.forEach(
        evaluation -> {
          String creatorId = evaluation.getCreatorId();
          String shopId = evaluation.getShopId();
          Map<String, Double> shopRatings =
              userShopMatrix.computeIfAbsent(creatorId, k -> new HashMap<>());

          shopRatings.merge(
              shopId,
              (double) evaluation.getRating(),
              (oldRating, newRating) -> (newRating + oldRating) / 2);
        });

    return userShopMatrix;
  }

  /**
   * Calculates similarity scores between the current user and other users based on their ratings.
   *
   * @param userMatrix the user evaluation matrix (product or shop).
   * @param currentUserRatings the current user's ratings.
   * @param userId the ID of the current user.
   * @return a map where the key is the other user ID and the value is the similarity score.
   */
  private Map<String, Double> calculateSimilarityScores(
      Map<String, Map<String, Double>> userMatrix,
      Map<String, Double> currentUserRatings,
      String userId) {
    Map<String, Double> similarityScores = new HashMap<>();

    userMatrix.forEach(
        (otherUserId, itemRatings) -> {
          if (!otherUserId.equals(userId)) {
            Double similarity = calculateCosineSimilarity(currentUserRatings, itemRatings);
            if (similarity > 0) {
              similarityScores.put(otherUserId, similarity);
            }
          }
        });

    return similarityScores;
  }

  /**
   * Generates recommendations based on similarity scores and user evaluations.
   *
   * @param similarityScores the similarity scores between the current user and other users.
   * @param userMatrix the user evaluation matrix (product or shop).
   * @param currentUserRatings the current user's ratings.
   * @return a list of recommended item IDs.
   */
  private List<String> generateRecommendations(
      Map<String, Double> similarityScores,
      Map<String, Map<String, Double>> userMatrix,
      Map<String, Double> currentUserRatings) {
    return similarityScores.entrySet().stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .flatMap(entry -> userMatrix.get(entry.getKey()).entrySet().stream())
        .filter(entry -> !currentUserRatings.containsKey(entry.getKey()))
        .map(Map.Entry::getKey)
        .distinct()
        .collect(Collectors.toList());
  }

  /**
   * Calculates the cosine similarity between two users' ratings.
   *
   * @param userRatings the current user's ratings.
   * @param otherUserRatings the other user's ratings.
   * @return the cosine similarity score.
   */
  private double calculateCosineSimilarity(
      Map<String, Double> userRatings, Map<String, Double> otherUserRatings) {
    double dotProduct = 0; // A.B
    double normA = 0; // |A|
    double normB = 0; // |B|

    for (String objId : userRatings.keySet()) {
      double userRating = userRatings.get(objId);
      double otherRating = otherUserRatings.getOrDefault(objId, 0.0);
      dotProduct += userRating * otherRating;
      normA += Math.pow(userRating, 2);
      normB += Math.pow(otherRating, 2);
    }

    if (normA == 0 || normB == 0) {
      return 0;
    }
    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
  }
}
