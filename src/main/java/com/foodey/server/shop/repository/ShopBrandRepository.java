package com.foodey.server.shop.repository;

import com.foodey.server.shop.model.ShopBrand;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopBrandRepository extends MongoRepository<ShopBrand, String> {
  boolean existsByNameAndOwnerId(String name, String ownerId);

  boolean existsByIdAndOwnerId(String name, String ownerId);

  Slice<ShopBrand> findByOwnerId(String ownerId, Pageable pageable);
}
