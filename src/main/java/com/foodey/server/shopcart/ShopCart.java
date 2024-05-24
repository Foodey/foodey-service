package com.foodey.server.shopcart;

import com.foodey.server.utils.PrincipalUtils;
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

  private long numberOfItems;

  private Map<String, Long> productsWithQuantity;

  public ShopCart(String userId, String shopId) {
    this.id = id(userId, shopId);
    this.numberOfItems = 0;
    this.productsWithQuantity = new HashMap<>();
  }

  public ShopCart(String shopId) {
    this(PrincipalUtils.getUserId(), shopId);
  }

  public static String id(String userId, String shopId) {
    return userId + "-" + shopId;
  }
}

// package com.foodey.server.shopcart;

// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import org.springframework.data.annotation.Id;
// import org.springframework.data.redis.core.RedisHash;
// import org.springframework.data.redis.core.index.Indexed;

// @Getter
// @Setter
// @RedisHash(value = "shopcart", timeToLive = 24 * 60 * 60)
// @NoArgsConstructor
// public class ShopCart {

//   @Id private String id;

//   @Indexed private String userId;

//   @Indexed private String shopId;

//   private double totalPrice;
//   private long numberOfItems;

//   public ShopCart(String userId, String shopId) {
//     this.id = id(userId, shopId);
//     this.userId = userId;
//     this.shopId = shopId;
//     this.totalPrice = 0;
//     this.numberOfItems = 0;
//   }

//   public static String id(String userId, String shopId) {
//     return userId + "-" + shopId;
//   }
// }
