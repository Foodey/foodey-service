package com.foodey.server.shop.repository;

import com.foodey.server.shop.model.Shop;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends MongoRepository<Shop, String> {

  boolean existsByNameAndBrandId(String name, String brandId);

  Optional<Shop> findByIdAndOwnerId(String id, String ownerId);

  Optional<Shop> findByIdAndBrandIdAndOwnerId(String id, String brandId, String ownerId);

  Optional<Shop> findByIdAndBrandId(String id, String brandId);

  List<Shop> findByBrandId(String brandId);

  Slice<Shop> findByBrandId(String brandId, Pageable pageable);

  Slice<Shop> findByCategoryIdsContaining(String categoryId, Pageable pageable);

  Slice<Shop> findByIdIn(Iterable<String> ids, Pageable pageable);
}
