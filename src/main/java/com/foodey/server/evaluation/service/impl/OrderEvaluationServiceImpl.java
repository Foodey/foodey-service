package com.foodey.server.evaluation.service.impl;

import com.foodey.server.evaluation.model.BaseEvaluation;
import com.foodey.server.evaluation.model.EvaluationType;
import com.foodey.server.evaluation.model.OrderEvaluation;
import com.foodey.server.evaluation.model.ProductEvaluation;
import com.foodey.server.evaluation.repository.OrderEvaluationRepository;
import com.foodey.server.evaluation.repository.ProductEvaluationRepository;
import com.foodey.server.evaluation.service.EvaluationService;
import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.order.Order;
import com.foodey.server.order.OrderItem;
import com.foodey.server.order.OrderService;
import com.foodey.server.user.model.User;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service(EvaluationType.Fields.ORDER)
public class OrderEvaluationServiceImpl extends EvaluationService {

  private final OrderEvaluationRepository orderEvaluationRepository;
  private final ProductEvaluationRepository productEvaluationRepository;
  private final OrderService orderService;

  @Override
  @SuppressWarnings("unchecked")
  public <T extends BaseEvaluation> T evaluate(User user, T evaluation) {
    assert evaluation instanceof OrderEvaluation;
    assert user != null;

    String userId = user.getId();
    String userName = user.getName();

    OrderEvaluation orderEvaluation = (OrderEvaluation) evaluation;
    String orderId = orderEvaluation.getOrderId();
    Order order = orderService.findByIdAndVerifyOwner(orderId, user);

    String shopId = order.getShopId() != null ? order.getShopId() : order.getShop().getId();

    List<ProductEvaluation> productEvaluations = orderEvaluation.getProductEvaluations();

    final byte rating = orderEvaluation.getRating() != null ? orderEvaluation.getRating() : 5;

    if (productEvaluations == null || productEvaluations.isEmpty()) {
      productEvaluationRepository.insert(
          order.getItems().stream()
              .map(
                  item ->
                      new ProductEvaluation(
                          orderId, shopId, item.getProductId(), rating, "", userName, userId))
              .toList());
    } else {
      List<OrderItem> items = order.getItems();

      Set<ProductEvaluation> evaluations =
          items.stream()
              .filter(
                  item ->
                      productEvaluations.stream()
                          .noneMatch(e -> e.getProductId().equals(item.getProductId())))
              .map(
                  item ->
                      new ProductEvaluation(
                          orderId, shopId, item.getProductId(), rating, "", userName, userId))
              .collect(Collectors.toCollection(HashSet::new));
      evaluations.addAll(
          productEvaluations.stream()
              .map(
                  e ->
                      new ProductEvaluation(
                          orderId,
                          shopId,
                          e.getProductId(),
                          e.getRating(),
                          e.getComment(),
                          userName,
                          userId))
              .collect(Collectors.toCollection(HashSet::new)));
      productEvaluationRepository.insert(evaluations);

      orderEvaluation.setRating(
          (byte) evaluations.stream().mapToInt(ProductEvaluation::getRating).average().orElse(5));
    }

    orderEvaluation.setCreatorId(user.getId());
    orderEvaluation.setCreatorName(user.getName());
    orderEvaluation.setShopId(shopId);

    try {
      return (T) orderEvaluationRepository.save(orderEvaluation);
    } catch (DuplicateKeyException e) {
      throw new ResourceAlreadyInUseException(
          "Order evaluation already exists for order " + orderId,
          "OrderEvaluation",
          "orderId",
          orderId);
    }
  }

  // WARN: We need to check the authority of the shop owner when shop owner find ther evaluation and
  // view it (may be shop can have many staff and we need to handle it later)
  @Override
  public BaseEvaluation findByOrderId(String orderId) {
    return orderEvaluationRepository
        .findByOrderId(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("Order Evaluation", "orderId", orderId));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends BaseEvaluation> List<T> findByShopIdInAndRated(
      Iterable<String> shopIds, boolean rated) {
    return (List<T>) orderEvaluationRepository.findByShopIdInAndRated(shopIds, rated);
  }
}
