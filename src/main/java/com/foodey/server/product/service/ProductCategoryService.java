package com.foodey.server.product.service;

import com.foodey.server.product.model.ProductCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductCategoryService {

  ProductCategory createProductCategory(ProductCategory productCategory);

  ProductCategory findById(String id);

  Slice<ProductCategory> findAll(Pageable pageable);

  boolean existsByName(String name);

  boolean existsById(String id);

  void deleteById(String id);
}
