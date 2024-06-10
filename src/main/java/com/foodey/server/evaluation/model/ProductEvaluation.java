package com.foodey.server.evaluation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.mongodb.core.index.Indexed;

public class ProductEvaluation extends BaseEvaluation {

  // @Indexed(unique = true)
  @Schema(
      description = "Order ID, Only one evaluation for one orderId for type ORDER",
      example = "5f7b1b7b7f7b1b7b7f7b1b7b")
  private String orderId;

  @Indexed private String shopId;

  public ProductEvaluation() {
    super(EvaluationType.PRODUCT);
  }
}
