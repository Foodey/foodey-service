package com.foodey.server.product.service;

import com.foodey.server.product.model.Product;
import com.foodey.server.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductService {

  Product createProduct(Product product, User user);

  Product findById(String id);

  Slice<Product> findAll(Pageable pageable);

  Product save(Product product);
}
