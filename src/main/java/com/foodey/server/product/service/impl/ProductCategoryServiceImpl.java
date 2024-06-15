package com.foodey.server.product.service.impl;

import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.product.model.ProductCategory;
import com.foodey.server.product.repository.ProductCategoryRepository;
import com.foodey.server.product.service.ProductCategoryService;
import com.foodey.server.upload.service.CloudinaryService;
import java.util.Map;
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
  private final CloudinaryService cloudinaryService;

  @Override
  @CacheEvict(value = "productCategories", allEntries = true)
  public ProductCategory createProductCategory(ProductCategory productCategory) {
    if (productCategoryRepository.existsByName(productCategory.getName())) {
      throw new ResourceAlreadyInUseException("ProductCategory", "name", productCategory.getName());
    }

    ProductCategory newProductCategory = productCategoryRepository.save(productCategory);
    newProductCategory.setCldImageUploadApiOptions(
        cloudinaryService.getUploadApiOptions(newProductCategory.getCldImage()));
    return newProductCategory;
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
    return productCategoryRepository.findByDeleted(false, pageable);
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
  @CacheEvict(value = "productCategories", allEntries = true)
  public void deleteById(String id) {
    productCategoryRepository.deleteById(id);
  }

  @Override
  public Map<String, Object> getImageUploadApiOptions(String id) {
    ProductCategory productCategory = findById(id);
    return cloudinaryService.getUploadApiOptions(productCategory.getCldImage());
  }

  @Override
  public ProductCategory save(ProductCategory productCategory) {
    return productCategoryRepository.save(productCategory);
  }
}
