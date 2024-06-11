package com.foodey.server.recommendation;

import com.foodey.server.evaluation.model.ProductEvaluation;
import com.foodey.server.evaluation.repository.ProductEvaluationRepository;
import com.foodey.server.product.model.Product;
import com.foodey.server.product.repository.ProductRepository;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RecommendationServiceImpl implements RecommendationService {

  private final ProductEvaluationRepository productEvaluationRepository;
  private final ProductRepository productRepository;

  @Override
  public Slice<Product> recommendProductsForUser(String userId, Pageable pageable) {

    final List<ProductEvaluation> pEvaluations = productEvaluationRepository.findAll();
    final Map<String, Map<String, Double>> userProductMatrix = new HashMap<>();

    // Build the user-product matrix
    pEvaluations.forEach(
        evaluation -> {
          String creatorId = evaluation.getCreatorId();
          String productId = evaluation.getProductId();

          // if user does not exist in the matrix, create a new entry
          // else, get the user and add the product and rating
          // Calculate the rating of the product for the user and add it to the matrix
          // if productId exists, update the rating by averaging the new rating with the existing
          // rating
          Map<String, Double> productRatings =
              userProductMatrix.computeIfAbsent(creatorId, (k) -> new HashMap<>());

          if (productRatings.containsKey(productId)) {
            double newRating = evaluation.getRating();
            double oldRating = productRatings.get(productId);
            productRatings.put(productId, (newRating + oldRating) / 2);
          } else {
            productRatings.put(productId, (double) evaluation.getRating());
          }
        });

    // Calculate similarities with other users
    Map<String, Double> similarityScores = new HashMap<>(); // key: userId, value: similarity
    Map<String, Double> currentUserProductRatings = userProductMatrix.get(userId);

    userProductMatrix.forEach(
        (otherUserId, itemRatings) -> {
          if (!otherUserId.equals(userId)) {
            Double similarity = cosineSimilarity(currentUserProductRatings, itemRatings);

            // Consider only positive similarities for recommendations
            if (similarity > 0) {
              similarityScores.put(otherUserId, similarity);
            }
          }
        });

    List<String> recommendedProductIds =
        similarityScores.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .flatMap(entry -> userProductMatrix.get(entry.getKey()).entrySet().stream())
            .filter(entry -> !currentUserProductRatings.containsKey(entry.getKey()))
            .map(entry -> entry.getKey())
            .distinct()
            .collect(Collectors.toList());

    return productRepository.findByIdIn(recommendedProductIds, pageable);
  }

  private double cosineSimilarity(
      Map<String, Double> userProductRatings, Map<String, Double> otherUserProductRatings) {
    double dotProduct = 0; // A.B
    double normA = 0; // |A|
    double normB = 0; // |B|

    for (String productId : userProductRatings.keySet()) {
      double userRating = userProductRatings.get(productId);
      double otherRating = otherUserProductRatings.getOrDefault(productId, 0.0);
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
