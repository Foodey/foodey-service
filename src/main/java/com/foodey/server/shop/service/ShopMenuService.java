package com.foodey.server.shop.service;

import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.model.ShopMenu;
import com.foodey.server.user.model.User;
import java.util.List;

public interface ShopMenuService {

  ShopMenu createShopMenu(ShopMenu shopMenu, String shopId, User user);

  ShopMenu findMenuInShop(String id, String shopId);

  List<ShopMenu> findAllInShop(String shopId);

  boolean existsByIdAndShopId(String id, String shopId);

  boolean existsByIdInShop(String id, Shop shop);
}
