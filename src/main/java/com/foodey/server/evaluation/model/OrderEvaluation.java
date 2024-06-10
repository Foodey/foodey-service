package com.foodey.server.evaluation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties({
  "shopId",
})
@Document(collection = "order_evaluations")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderEvaluation extends BaseEvaluation {

  @Schema(
      description = "Order ID, Only one evaluation for one orderId for type ORDER",
      example = "5f7b1b7b7f7b1b7b7f7b1b7b")
  @Indexed(unique = true)
  private String orderId;

  @Indexed private String shopId;

  @JsonProperty("productEvaluations")
  @Transient
  private List<@Valid ProductEvaluation> productEvaluations;

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
    this.productEvaluations = new java.util.ArrayList<>();
  }

  public OrderEvaluation(
      String orderId,
      String shopId,
      byte rating,
      String comment,
      String creatorName,
      String creatorId,
      List<ProductEvaluation> productEvaluations) {
    super(EvaluationType.ORDER, rating, comment, creatorName, creatorId);
    this.orderId = orderId;
    this.shopId = shopId;
    this.productEvaluations = productEvaluations;
  }
}
