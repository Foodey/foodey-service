package com.foodey.server.shop.service;

import com.foodey.server.shop.model.MenuResponse;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.model.ShopMenu;
import com.foodey.server.user.model.User;
import java.util.List;

public interface ShopMenuService {

  ShopMenu createShopMenu(ShopMenu shopMenu, String shopId, User user);

  boolean existsInShop(String id, String shopId);

  boolean existsInShop(String id, Shop shop);

  ShopMenu findMenuInShop(String id, String shopId);

  MenuResponse findMenuDetailsInShop(String id, String shopId);

  List<ShopMenu> findAllInShop(String shopId);

  List<MenuResponse> findAllDetailsInShop(String shopId);
}
