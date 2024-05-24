package com.foodey.server.shopcart;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.RedisHash;

/** ShopCart */
@Getter
@Setter
@Document(collection = "shopcarts")
@NoArgsConstructor
@RedisHash(value = "shopcart", timeToLive = 24 * 60 * 60)
public class ShopCart {
  @Id private String id;

  private double totalPrice;
  private long numberOfItems;

  private Map<String, Long> productsWithQuantity;

  public ShopCart(String userId, String shopId) {
    this.id = id(userId, shopId);
    this.totalPrice = 0;
    this.numberOfItems = 0;
    this.productsWithQuantity = new HashMap<>();
  }

  public static String id(String userId, String shopId) {
    return userId + "-" + shopId;
  }
}
