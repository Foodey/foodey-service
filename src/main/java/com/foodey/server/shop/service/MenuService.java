package com.foodey.server.shop.service;

import com.foodey.server.product.model.Product;
import com.foodey.server.product.model.ProductWithCloudinaryMetadata;
import com.foodey.server.shop.dto.MenuView;
import com.foodey.server.user.model.User;
import java.util.List;

public interface MenuService {
  List<Product> addProductsToMenu(
      User user, String brandId, String shopId, List<Product> products, boolean appliedToAllShops);

  ProductWithCloudinaryMetadata addProductToMenu(
      User user, String brandId, String shopId, Product product, boolean appliedToAllShops);

  MenuView getMenuInShop(String brandId, String shopId);

  MenuView getMenuInShop(String brandId, String shopId, String categoryName);

  MenuView getMenuForWholeBrand(String brandId);

  MenuView getMenuForWholeBrand(String brandId, String categoryName);
}
