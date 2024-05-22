package com.foodey.server.shop.repository;

import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.model.ShopMenu;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends MongoRepository<Shop, String> {

  boolean existsByNameAndBranchId(String name, String ownerId);

  Optional<Shop> findByIdAndOwnerId(String id, String ownerId);

  Optional<ShopMenu> findByIdAndMenusId(String id, String menuId);
}
