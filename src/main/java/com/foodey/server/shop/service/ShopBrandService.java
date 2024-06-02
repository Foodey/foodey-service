package com.foodey.server.shop.service;

import com.foodey.server.shop.model.ShopBrand;
import com.foodey.server.user.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ShopBrandService {

  ShopBrand createShopBrand(ShopBrand shopBrand, User user);

  ShopBrand save(ShopBrand shopBrand);

  ShopBrand findById(String brandId);

  List<ShopBrand> findByOwnerId(String ownerId);

  Slice<ShopBrand> findAll(Pageable pageable);

  boolean existsByIdAndOwnerId(String brandId, String ownerId);

  // access control

  ShopBrand findByIdAndVerifyOwner(String id, String userId);

  default ShopBrand findByIdAndVerifyOwner(String id, User user) {
    return findByIdAndVerifyOwner(id, user.getId());
  }

  void verifyOwner(String brachId, String userId);

  default void verifyOwner(String brachId, User user) {
    this.verifyOwner(brachId, user.getId());
  }
}
