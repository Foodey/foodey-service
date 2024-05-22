package com.foodey.server.product.service;

import com.foodey.server.product.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCategoryService {

  ProductCategory createProductCategory(ProductCategory productCategory);

  ProductCategory findById(String id);

  Page<ProductCategory> findAll(Pageable pageable);

  boolean existsByName(String name);

  boolean existsById(String id);
}
