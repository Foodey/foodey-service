package com.foodey.server.shop.service;

import com.foodey.server.shop.model.MenuResponse;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.model.ShopMenu;
import com.foodey.server.shop.model.ShopMenuFound;
import com.foodey.server.shop.model.ShopMenusContainer;
import com.foodey.server.user.model.User;
import java.util.List;

public interface ShopMenuService {

  ShopMenu createShopMenu(ShopMenu shopMenu, String shopId, User user);

  ShopMenu createShopMenuInAllBranch(ShopMenu shopMenu, String branchId, User user);

  boolean existsInShop(String id, String shopId);

  boolean existsInShop(String id, Shop shop);

  void validateMenuSize(ShopMenu menu);

  ShopMenuFound findMenuInShop(String id, String shopId);

  ShopMenuFound findMenuInShop(String id, Shop shop);

  MenuResponse findMenuDetailsInShop(String id, String shopId);

  List<ShopMenu> findAllInShop(String shopId);

  List<MenuResponse> findAllDetailsInShop(String shopId);

  void save(ShopMenusContainer container);
}
