package com.foodey.server.product.service.impl;

import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.product.model.ProductCategory;
import com.foodey.server.product.repository.ProductCategoryRepository;
import com.foodey.server.product.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

  private final ProductCategoryRepository productCategoryRepository;

  @Override
  @CacheEvict(value = "productCategories", allEntries = true, cacheManager = "redisCacheManager")
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
  @Cacheable(value = "productCategories", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
  public Slice<ProductCategory> findAll(Pageable pageable) {
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

  @Override
  public void deleteById(String id) {
    productCategoryRepository.deleteById(id);
  }
}
