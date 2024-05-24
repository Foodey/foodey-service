package com.foodey.server.shop.repository;

import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.model.ShopMenu;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends MongoRepository<Shop, String> {

  boolean existsByNameAndBranchId(String name, String ownerId);

  Optional<Shop> findByIdAndOwnerId(String id, String ownerId);

  Optional<ShopMenu> findByIdAndMenusIdContaining(String id, String menuId);

  Slice<Shop> findByCategoryIdsContaining(String categoryId, Pageable pageable);

  Slice<Shop> findByIdIn(Iterable<String> ids, Pageable pageable);
}
