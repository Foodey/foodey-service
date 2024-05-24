package com.foodey.server.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodey.server.payment.Payment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

/** Order */
@Getter
@Setter
@Document(collection = "orders")
@ToString
public class Order implements Persistable<String> {
  @Null @Id private String id;

  private String userId;

  private String shopId;

  private String shiperId;

  private String voucherCode;

  private String shippingAddress;

  @NotNull private OrderStatus status;

  private List<OrderItem> items;

  @NotNull private Payment payment;

  @CreatedDate private Instant createdAt;
  @LastModifiedDate private Instant updatedAt;

  public Order(
      String userId,
      String shopId,
      String shiperId,
      String shippingAddress,
      Payment payment,
      String voucherCode,
      List<OrderItem> items) {
    this.userId = userId;
    this.shopId = shopId;
    this.shiperId = shiperId;
    this.shippingAddress = shippingAddress;
    this.payment = payment;
    this.voucherCode = voucherCode;
    this.items = items;
    this.status = OrderStatus.PENDING;
  }

  @JsonIgnore
  @Override
  public boolean isNew() {
    return createdAt == null || id == null;
  }
}
