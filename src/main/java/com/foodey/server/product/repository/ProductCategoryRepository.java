package com.foodey.server.product.repository;

import com.foodey.server.product.model.ProductCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String> {

  Slice<ProductCategory> findByDeleted(Boolean deteled, Pageable pageable);

  boolean existsByName(String name);
}
