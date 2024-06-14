package com.foodey.server.evaluation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@JsonIgnoreProperties(
    value = {"shopId", "orderId"},
    allowGetters = true)
@AllArgsConstructor
@Document(collection = "product_evaluations")
@JsonTypeName(EvaluationType.Fields.PRODUCT)
public class ProductEvaluation extends BaseEvaluation {

  // @Indexed(unique = true)
  @Schema(
      description = "Order ID, Only one evaluation for one orderId for type ORDER",
      example = "5f7b1b7b7f7b1b7b7f7b1b7b")
  private String orderId;

  @Indexed private String shopId;

  @NotBlank private String productId;

  public ProductEvaluation() {
    super(EvaluationType.PRODUCT);
    super.setRated(true); // always rated
  }

  public ProductEvaluation(
      String orderId,
      String shopId,
      String productId,
      byte rating,
      String comment,
      String creatorName,
      String creatorId) {
    super(EvaluationType.PRODUCT, rating, comment, creatorName, creatorId);
    super.setRated(true); // always rated
    this.orderId = orderId;
    this.shopId = shopId;
    this.productId = productId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, shopId, productId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    else if (!(obj instanceof ProductEvaluation)) return false;
    ProductEvaluation that = (ProductEvaluation) (obj);
    return orderId.equals(that.orderId)
        && shopId.equals(that.shopId)
        && productId.equals(that.productId);
  }
}
