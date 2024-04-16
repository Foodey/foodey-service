package com.foodey.server.shop.repository;

import com.foodey.server.shop.model.ShopMenu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopMenuRepository extends MongoRepository<ShopMenu, String> {}
