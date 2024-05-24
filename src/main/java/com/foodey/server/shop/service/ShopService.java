package com.foodey.server.shop.service;

import com.foodey.server.shop.model.Shop;
import com.foodey.server.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ShopService {

  Shop createShop(Shop shop, User user);

  Shop findById(String id);

  Shop findByIdAndAutoAddBranchMenus(String id);

  Slice<Shop> findAll(Pageable pageable);

  Shop save(Shop shop);

  Shop findByIdAndVerifyOwner(String id, User user);

  Slice<Shop> findByCategoryId(String category, Pageable pageable);

  Slice<Shop> findByCategoryIdAndAutoAddBranchMenus(String category, Pageable pageable);
}
