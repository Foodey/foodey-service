package com.foodey.server.shop.service;

import com.foodey.server.shop.model.Shop;
import com.foodey.server.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShopService {

  Shop createShop(Shop shop, User user);

  Shop findById(String id);

  Shop findByIdAndAutoAddBranchMenus(String id);

  Page<Shop> findAll(Pageable pageable);

  Shop save(Shop shop);

  Shop findByIdAndVerifyOwner(String id, User user);
}
