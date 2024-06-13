package com.foodey.server.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodey.server.payment.Payment;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.user.model.User;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class Order implements Persistable<String> {
  @Id private String id;

  @JsonIgnore private String userId;

  private String userName;

  private String userPhoneNumber;

  @DBRef
  @Deprecated(since = "1.0.0")
  private Shop shop;

  private String shopId;

  private String shopName;

  private String note;

  private String shipperId;

  private String voucherCode;

  private double totalDiscount;

  private String voucherName;

  private String shippingAddress;

  // private Address address;

  @NotNull private OrderStatus status;

  private List<OrderItem> items;

  @NotNull private Payment payment;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;

  public Order(
      User user,
      Shop shop,
      String shopId,
      String shopName,
      String shipperId,
      String shippingAddress,
      Payment payment,
      String voucherCode,
      String voucherName,
      double totalDiscount,
      String note,
      List<OrderItem> items) {
    this.userId = user.getId();
    this.userName = user.getName();
    this.userPhoneNumber = user.getPhoneNumber();
    this.shop = shop;
    this.shopId = shopId;
    this.shopName = shopName;
    this.shipperId = shipperId;
    this.shippingAddress = shippingAddress;
    this.payment = payment;
    this.voucherCode = voucherCode;
    this.voucherName = voucherName;
    this.totalDiscount = totalDiscount;
    this.items = items;
    this.note = note == null ? "" : note;
    this.status = OrderStatus.PENDING;
  }

  public Order(
      String userId,
      String userName,
      String userPhoneNumber,
      Shop shop,
      String shopId,
      String shopName,
      String shipperId,
      String shippingAddress,
      Payment payment,
      String voucherCode,
      String voucherName,
      double totalDiscount,
      String note,
      List<OrderItem> items) {
    this.userId = userId;
    this.userName = userName;
    this.userPhoneNumber = userPhoneNumber;
    this.shop = shop;
    this.shopId = shopId;
    this.shopName = shopName;
    this.shipperId = shipperId;
    this.shippingAddress = shippingAddress;
    this.payment = payment;
    this.voucherCode = voucherCode;
    this.voucherName = voucherName;
    this.totalDiscount = totalDiscount;
    this.items = items;
    this.note = note == null ? "" : note;
    this.status = OrderStatus.PENDING;
  }

  @JsonIgnore
  @Override
  public boolean isNew() {
    return createdAt == null || id == null;
  }
}
