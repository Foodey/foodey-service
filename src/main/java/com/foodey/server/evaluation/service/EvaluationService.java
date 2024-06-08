package com.foodey.server.evaluation.service;

import com.foodey.server.evaluation.model.BaseEvaluation;
import com.foodey.server.user.model.User;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;

public abstract class EvaluationService {

  public abstract <T extends BaseEvaluation> T evaluate(User user, T evaluation);

  @Async
  public <T extends BaseEvaluation> CompletableFuture<BaseEvaluation> asyncEvaluate(
      User user, T evaluation) {
    return CompletableFuture.supplyAsync(() -> evaluate(user, evaluation));
  }

  public abstract BaseEvaluation findByOrderId(String orderId);

  public abstract <T extends BaseEvaluation> List<T> findByShopIdInAndRated(
      Iterable<String> shopIds, boolean rated);
}
