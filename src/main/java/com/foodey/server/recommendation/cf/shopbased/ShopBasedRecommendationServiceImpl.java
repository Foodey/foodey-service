package com.foodey.server.recommendation.cf.shopbased;

import com.foodey.server.evaluation.repository.OrderEvaluationRepository;
import com.foodey.server.evaluation.repository.ProductEvaluationRepository;
import com.foodey.server.product.repository.ProductRepository;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.repository.ShopRepository;
import com.foodey.server.shop.service.ShopService;
import com.foodey.server.user.model.User;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShopBasedRecommendationServiceImpl implements ShopBasedRecommendationService {

  private final ProductEvaluationRepository productEvaluationRepository;
  private final OrderEvaluationRepository orderEvaluationRepository;
  private final ProductRepository productRepository;
  private final ShopRepository shopRepository;
  private final ShopService shopService;

  /**
   * Recommends shops for a user based on their and others' shop evaluations.
   *
   * @param userId the ID of the user for whom recommendations are being generated.
   * @param pageable the pagination information.
   * @return a slice of recommended shops.
   */
  @Override
  public Slice<Shop> recommendShopsForUser(User user, Pageable pageable) {
    List<Shop> allShops = shopRepository.findAll();

    List<Shop> favoriteShops = shopService.findAllById(user.getFavoriteShopIds());

    if (favoriteShops.isEmpty()) {
      return new SliceImpl<>(new ArrayList<>(), pageable, false);
    }

    double[] avgFeatures = calculateAverageFeatures(favoriteShops);

    // Find shops that are similar to the average features
    // PriorityQueue<Shop> maxHeap =
    //     new PriorityQueue<>(
    //         Comparator.comparingDouble(
    //             shop -> -calculateCosineSimilarity(shop.getFeatures(), avgFeatures)));

    // for (Shop shop : allShops) {
    //   if (!user.getFavoriteShopIds().contains(shop.getId())) { // Exclude user's favorite shops
    //     maxHeap.add(shop);
    //     if (maxHeap.size() > pageable.getPageSize()) {
    //       maxHeap.poll();
    //     }
    //   }
    // }

    return null;
  }

  /**
   * Calculate the average feature vector of user's favorite shops.
   *
   * @param favoriteShops list of favorite shops of the user
   * @return average feature vector
   */
  private double[] calculateAverageFeatures(List<Shop> favoriteShops) {
    int featureSize = favoriteShops.get(0).getFeatures().length;
    double[] avgFeatures = new double[featureSize];

    for (Shop shop : favoriteShops) {
      for (int i = 0; i < featureSize; i++) {
        avgFeatures[i] += shop.getFeatures()[i];
      }
    }

    for (int i = 0; i < featureSize; i++) {
      avgFeatures[i] /= favoriteShops.size();
    }

    return avgFeatures;
  }

  @Override
  public Slice<Shop> recommendShopsForUser(
      User user, double longitude, double latitude, long maxDistanceKms, Pageable pageable) {
    return null;
  }

  /**
   * Calculate cosine similarity between two feature vectors.
   *
   * @param features1 feature vector 1
   * @param features2 feature vector 2
   * @return cosine similarity score
   */
  private double calculateCosineSimilarity(double[] features1, double[] features2) {
    double dotProduct = 0.0;
    double norm1 = 0.0;
    double norm2 = 0.0;

    for (int i = 0; i < features1.length; i++) {
      dotProduct += features1[i] * features2[i];
      norm1 += Math.pow(features1[i], 2);
      norm2 += Math.pow(features2[i], 2);
    }

    if (norm1 == 0 || norm2 == 0) {
      return 0.0; // Handle division by zero
    }

    return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
  }
}
