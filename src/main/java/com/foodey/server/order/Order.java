package com.foodey.server.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.foodey.server.payment.Payment;
import com.foodey.server.shop.model.Shop;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/** Order */
@Getter
@Setter
@ToString
@Document(collection = "orders")
@CompoundIndexes({
  @CompoundIndex(name = "user_status_idx", def = "{'userId': 1, 'status': 1}"),
  @CompoundIndex(name = "shop_status_idx", def = "{'shop.$id': 1, 'status': 1}")
})
public class Order implements Persistable<String> {
  @Id private String id;

  private String userId;

  @DBRef private Shop shop;

  private String shipperId;

  private String voucherCode;

  private String shippingAddress;

  @NotNull private OrderStatus status;

  private List<OrderItem> items;

  @NotNull private Payment payment;

  @JsonSerialize(using = InstantSerializer.class)
  @CreatedDate
  private Instant createdAt;

  @JsonSerialize(using = InstantSerializer.class)
  @LastModifiedDate
  private Instant updatedAt;

  public Order(
      String userId,
      Shop shop,
      String shipperId,
      String shippingAddress,
      Payment payment,
      String voucherCode,
      List<OrderItem> items) {
    this.userId = userId;
    this.shop = shop;
    this.shipperId = shipperId;
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
