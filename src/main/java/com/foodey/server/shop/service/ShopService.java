package com.foodey.server.shop.service;

import com.foodey.server.shop.model.Shop;
import com.foodey.server.user.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ShopService {

  Shop createShop(Shop shop, User user);

  Shop findById(String id);

  Slice<Shop> findAll(Pageable pageable);

  Shop save(Shop shop);

  List<Shop> saveAll(List<Shop> shops);

  default Shop findByIdAndVerifyOwner(String id, User user) {
    return findByIdAndVerifyOwner(id, user.getId());
  }

  Shop findByIdAndVerifyOwner(String id, String userId);

  Slice<Shop> findByCategoryId(String category, Pageable pageable);

  List<Shop> findByBrandId(String brandId);

  Slice<Shop> findByBrandId(String brandId, Pageable pageable);
}
