package com.foodey.server.shop.service;

import com.foodey.server.product.model.Product;
import com.foodey.server.shop.dto.MenuView;
import com.foodey.server.shop.model.Menu;
import com.foodey.server.user.model.User;
import java.util.List;

public interface MenuService {
  List<Product> addProductsToMenu(
      User user, String brandId, String shopId, List<Product> products, boolean appliedToAllShops);

  Product addProductToMenu(
      User user, String brandId, String shopId, Product product, boolean appliedToAllShops);

  void validateMenuSize(Menu menu);

  MenuView getMenuInShop(String brandId, String shopId);

  MenuView getMenuInShop(String brandId, String shopId, String categoryName);

  MenuView getMenuInBrand(String brandId);

  MenuView getMenuInBrand(String brandId, String categoryName);
}
