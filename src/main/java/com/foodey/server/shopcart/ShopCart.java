package com.foodey.server.shopcart;

import com.foodey.server.utils.PrincipalUtils;
import com.foodey.server.voucher.Voucher;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@ToString
@RedisHash(value = "shopcart", timeToLive = 24 * 60 * 60)
// @Document(collection = "shopcarts")
public class ShopCart {
  @Id private String id;

  private String shopId;

  private String userId;

  private long numberOfItems;

  private Voucher voucher;

  public void setVoucher(Voucher voucher) {
    this.voucher = voucher;
  }

  public Voucher getVoucher() {
    return voucher;
  }

  private Map<String, Long> productsWithQuantity;

  public ShopCart(String userId, String shopId) {
    this.id = id(userId, shopId);
    this.shopId = shopId;
    this.userId = userId;
    this.numberOfItems = 0;
    this.productsWithQuantity = new HashMap<>();
  }

  public Map<String, Long> getProductsWithQuantity() {
    if (productsWithQuantity == null) productsWithQuantity = new HashMap<>();
    // return productsWithQuantity;
    return Collections.unmodifiableMap(productsWithQuantity);
  }

  public void addProduct(String productId, long quantity) {
    if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero.");

    productsWithQuantity.merge(productId, quantity, Long::sum);
    numberOfItems += quantity;
  }

  public void removeProduct(String productId, long quantity) {
    if (quantity <= 0) throw new IllegalArgumentException("Invalid quantity to remove.");

    Long currentQuantity = productsWithQuantity.get(productId);
    if (currentQuantity == null) return; // no such product in the cart
    else if (quantity >= currentQuantity) {
      productsWithQuantity.remove(productId);
      numberOfItems -= currentQuantity;
    } else {
      productsWithQuantity.put(productId, currentQuantity - quantity);
      numberOfItems -= quantity;
    }
  }

  public void removeProduct(String productId) {
    Long currentQuantity = productsWithQuantity.get(productId);
    if (currentQuantity != null) {
      productsWithQuantity.remove(productId);
      numberOfItems -= currentQuantity;
    }
  }

  public void setProductQuantity(String productId, long quantity) {
    if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero.");

    Long currentQuantity = productsWithQuantity.get(productId);
    if (currentQuantity == null) {
      addProduct(productId, quantity);
    } else {
      numberOfItems += quantity - currentQuantity;
      productsWithQuantity.put(productId, quantity);
    }
  }

  public long getQuantity(String productId) {
    return productsWithQuantity.getOrDefault(productId, 0L);
  }

  public void clearCart() {
    productsWithQuantity.clear();
    numberOfItems = 0;
  }

  // Check if the cart is empty
  public boolean isEmpty() {
    return numberOfItems == 0;
  }

  public ShopCart(String shopId) {
    this(PrincipalUtils.getUserId(), shopId);
  }

  public static String id(String userId, String shopId) {
    return userId + "-" + shopId;
  }

  // Implement hashCode and equals based on id
  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    ShopCart shopCart = (ShopCart) obj;
    return id.equals(shopCart.id);
  }
}
