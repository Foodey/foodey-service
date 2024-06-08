package com.foodey.server.evaluation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties({"shopId"})
@Document(collection = "order_evaluations")
public class OrderEvaluation extends BaseEvaluation {

  @Indexed(unique = true)
  @Schema(
      description = "Order ID, Only one evaluation for one orderId for type ORDER",
      example = "5f7b1b7b7f7b1b7b7f7b1b7b")
  private String orderId;

  @Indexed private String shopId;

  public OrderEvaluation() {
    super(EvaluationType.ORDER);
  }

  public OrderEvaluation(
      String orderId,
      String shopId,
      byte rating,
      String comment,
      String creatorName,
      String creatorId) {
    super(EvaluationType.ORDER, rating, comment, creatorName, creatorId);
    this.orderId = orderId;
    this.shopId = shopId;
  }
}
