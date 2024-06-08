package com.foodey.server.evaluation.service.impl;

import com.foodey.server.evaluation.model.BaseEvaluation;
import com.foodey.server.evaluation.model.EvaluationType;
import com.foodey.server.evaluation.model.OrderEvaluation;
import com.foodey.server.evaluation.repository.OrderEvaluationRepository;
import com.foodey.server.evaluation.service.EvaluationService;
import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.order.Order;
import com.foodey.server.order.OrderService;
import com.foodey.server.user.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service(EvaluationType.Fields.ORDER)
public class OrderEvaluationServiceImpl extends EvaluationService {

  private final OrderEvaluationRepository orderEvaluationRepository;
  private final OrderService orderService;

  @Override
  @SuppressWarnings("unchecked")
  public <T extends BaseEvaluation> T evaluate(User user, T evaluation) {
    assert evaluation instanceof OrderEvaluation;
    assert user != null;

    OrderEvaluation orderEvaluation = (OrderEvaluation) evaluation;

    String orderId = orderEvaluation.getOrderId();
    Order order = orderService.findByIdAndVerifyOwner(orderId, user);

    orderEvaluation.setCreatorId(user.getId());
    orderEvaluation.setCreatorName(user.getName());
    orderEvaluation.setShopId(
        order.getShopId() != null ? order.getShopId() : order.getShop().getId());

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
