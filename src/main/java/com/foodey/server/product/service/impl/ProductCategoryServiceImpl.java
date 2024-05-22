package com.foodey.server.product.service.impl;

import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.product.model.ProductCategory;
import com.foodey.server.product.repository.ProductCategoryRepository;
import com.foodey.server.product.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/** ProductCategoryServiceImpl */
@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

  private final ProductCategoryRepository productCategoryRepository;

  @Override
  public ProductCategory createProductCategory(ProductCategory productCategory) {
    if (productCategoryRepository.existsByName(productCategory.getName())) {
      throw new ResourceAlreadyInUseException("ProductCategory", "name", productCategory.getName());
    }
    return productCategoryRepository.save(productCategory);
  }

  @Override
  public ProductCategory findById(String id) {
    return productCategoryRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("ProductCategory", "id", id));
  }

  @Override
  public Page<ProductCategory> findAll(Pageable pageable) {
    return productCategoryRepository.findAll(pageable);
  }

  @Override
  public boolean existsByName(String name) {
    return productCategoryRepository.existsByName(name);
  }

  @Override
  public boolean existsById(String id) {
    return productCategoryRepository.existsById(id);
  }
}
