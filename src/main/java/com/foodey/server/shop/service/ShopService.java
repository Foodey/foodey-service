package com.foodey.server.shop.service;

import com.foodey.server.shop.model.Shop;
import com.foodey.server.user.model.User;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ShopService {

  Shop createShop(Shop shop, User user);

  Shop findById(String id);

  List<Shop> findAllById(Iterable<String> ids);

  Shop findByIdAndBrandIdAndVerifyOwner(String id, String brandId, String userId);

  Slice<Shop> findAll(Pageable pageable);

  Slice<Shop> findAllByIdNear(
      Iterable<String> ids,
      double longitude,
      double latitude,
      long maxDistanceKms,
      Pageable pageable);

  Slice<Shop> findAllNear(
      double longitude, double latitude, long maxDistanceKms, Pageable pageable);

  Shop save(Shop shop);

  List<Shop> saveAll(Iterable<Shop> shops);

  Shop findByIdAndVerifyOwner(String id, String userId);

  default Shop findByIdAndVerifyOwner(String id, User user) {
    return findByIdAndVerifyOwner(id, user.getId());
  }

  Slice<Shop> findByCategoryId(String category, Pageable pageable);

  Slice<Shop> findByCategoryIdNear(
      String category, double longitude, double latitude, long maxDistanceKms, Pageable pageable);

  List<Shop> findByBrandId(String brandId);

  Slice<Shop> findByBrandId(String brandId, Pageable pageable);

  Slice<Shop> searchByName(String query, Pageable pageable);

  Slice<Shop> searchByName(
      String query, double longitude, double latitude, long maxDistanceKms, Pageable pageable);

  List<Shop> getShopsNotRatedSince(Instant date, long limit);

  Slice<Shop> findByOwnerId(String ownerId, Pageable pageable);

  Map<String, Object> getLogoUploadApiOptions(String shopId, String userId);

  Map<String, Object> getWallpaperUploadApiOptions(String shopId, String userId);
}
