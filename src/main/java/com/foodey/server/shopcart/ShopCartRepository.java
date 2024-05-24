package com.foodey.server.shopcart;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopCartRepository extends CrudRepository<ShopCart, String> {}
