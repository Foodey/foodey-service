package com.foodey.server.evaluation.service;

import com.foodey.server.evaluation.model.BaseEvaluation;
import com.foodey.server.user.model.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EvaluationServiceFactory {

  private final Map<String, EvaluationService> orderEvaluationServices;

  public Optional<EvaluationService> getService(String type) {
    return Optional.ofNullable(orderEvaluationServices.get(type));
  }

  public <T extends BaseEvaluation> T evaluate(String type, User user, T evaluation) {
    return getService(type)
        .orElseThrow(() -> new IllegalArgumentException("No service found for type: " + type))
        .evaluate(user, evaluation);
  }

  @Async
  public <T extends BaseEvaluation> CompletableFuture<T> asyncEvaluate(
      String type, User user, T evaluation) {
    return CompletableFuture.supplyAsync(() -> evaluate(type, user, evaluation));
  }

  public BaseEvaluation findByOrderId(String type, String orderId) {
    return getService(type)
        .orElseThrow(() -> new IllegalArgumentException("No service found for type: " + type))
        .findByOrderId(orderId);
  }
}
