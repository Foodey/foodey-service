package com.foodey.server.evaluation.repository;

import com.foodey.server.evaluation.model.OrderEvaluation;
import com.foodey.server.evaluation.model.ProductEvaluation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductEvaluationRepository extends MongoRepository<ProductEvaluation, String> {

  Optional<OrderEvaluation> findByOrderIdAndCreatorId(String orderId, String creatorId);

  List<ProductEvaluation> findByOrderId(String orderId);

  List<ProductEvaluation> findByShopId(String shopId);
}
