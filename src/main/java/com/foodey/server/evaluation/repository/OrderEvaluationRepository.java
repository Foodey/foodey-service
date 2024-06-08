package com.foodey.server.evaluation.repository;

import com.foodey.server.evaluation.model.OrderEvaluation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/** OrderEvaluationRepository */
public interface OrderEvaluationRepository extends MongoRepository<OrderEvaluation, String> {

  Optional<OrderEvaluation> findByOrderIdAndCreatorId(String orderId, String creatorId);

  Optional<OrderEvaluation> findByOrderId(String orderId);

  List<OrderEvaluation> findByShopId(String shopId);

  List<OrderEvaluation> findByShopIdInAndRated(Iterable<String> shopIds, boolean rated);
}
