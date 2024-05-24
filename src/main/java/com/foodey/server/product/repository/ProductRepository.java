package com.foodey.server.product.repository;

import com.foodey.server.product.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

  Slice<Product> findByIdIn(Iterable<String> ids, Pageable pageable);
}
