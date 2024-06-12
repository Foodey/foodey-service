package com.foodey.server.shopcart;

import com.foodey.server.utils.PrincipalUtils;
import java.util.Optional;

public interface ShopCartService {

  Optional<ShopCart> find(String userId, String shopId);

  ShopCart findOrCreate(String userId, String shopId);

  ShopCart save(ShopCart shopCart);

  void delete(String userId, String shopId);

  ShopCartDetail getDetail(String userId, String shopId);

  default ShopCartDetail getDetail(String shopId) {
    return getDetail(PrincipalUtils.getUser().getId(), shopId);
  }

  void adjustProduct(
      String userId, String shopId, String productId, long quantity, ShopCartProductAction action);

  default void adjustProduct(
      String shopId, String productId, long quantity, ShopCartProductAction action) {
    adjustProduct(PrincipalUtils.getUser().getId(), shopId, productId, quantity, action);
  }

  boolean removeProduct(String userId, String shopId, String productId);

  default boolean removeProduct(String shopId, String productId) {
    return removeProduct(PrincipalUtils.getUser().getId(), shopId, productId);
  }
  ;

  void clear(String userId, String shopId);
}
