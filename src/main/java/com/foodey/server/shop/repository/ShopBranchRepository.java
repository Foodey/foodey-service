package com.foodey.server.shop.repository;

import com.foodey.server.shop.model.ShopBranch;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopBranchRepository extends MongoRepository<ShopBranch, String> {
  boolean existsByNameAndOwnerId(String name, String ownerId);

  List<ShopBranch> findByOwnerId(String ownerId);
}
