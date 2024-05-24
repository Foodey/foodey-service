package com.foodey.server.shopcart;

import java.util.Optional;

public interface ShopCartService {

  Optional<ShopCart> find(String userId, String shopId);

  ShopCart findOrCreate(String userId, String shopId);

  ShopCart update(ShopCart shopCart);

  ShopCart save(ShopCart shopCart);

  void delete(String userId, String shopId);

  ShopCartDetail getDetail(String userId, String shopId);

  ShopCartDetail getDetail(String shopId);

  void adjustProduct(
      String userId, String shopId, String productId, long quantity, ShopCartProductAction action);

  void adjustProduct(String shopId, String productId, long quantity, ShopCartProductAction action);

  boolean removeProduct(String userId, String shopId, String productId);

  boolean removeProduct(String shopId, String productId);
}
