package com.foodey.server.product.repository;

import com.foodey.server.product.model.ProductCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String> {

  boolean existsByName(String name);
}
