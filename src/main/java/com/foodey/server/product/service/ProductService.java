package com.foodey.server.product.service;

import com.foodey.server.product.model.Product;
import com.foodey.server.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

  Product createProduct(Product product, User user);

  Product findById(String id);

  Page<Product> findAll(Pageable pageable);

  Product save(Product product);

  Page<Product> findByShopIdAndMenuId(String shopId, String menuId, Pageable pageable);
}
