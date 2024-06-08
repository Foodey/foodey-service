package com.foodey.server.evaluation.controller;

import com.foodey.server.annotation.CurrentUser;
import com.foodey.server.evaluation.model.BaseEvaluation;
import com.foodey.server.evaluation.model.EvaluationType;
import com.foodey.server.evaluation.service.EvaluationServiceFactory;
import com.foodey.server.user.enums.RoleType;
import com.foodey.server.user.model.User;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/evaluations")
public class EvaluationController {

  private final EvaluationServiceFactory evaluationServiceFactory;

  @RolesAllowed(RoleType.Fields.CUSTOMER)
  @PostMapping("")
  public BaseEvaluation evaluate(
      @CurrentUser User user, @RequestBody @Valid BaseEvaluation evaluation) {
    return evaluationServiceFactory.evaluate(evaluation.getType().name(), user, evaluation);
  }

  @GetMapping("/orders/{orderId}")
  @RolesAllowed(RoleType.Fields.CUSTOMER)
  public BaseEvaluation findByOrderId(
      @RequestParam("type") EvaluationType type, @PathVariable("orderId") String orderId) {
    return evaluationServiceFactory.findByOrderId(type.name(), orderId);
  }
}
