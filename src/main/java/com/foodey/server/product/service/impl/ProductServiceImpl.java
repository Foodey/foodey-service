package com.foodey.server.product.service.impl;

import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.product.model.Product;
import com.foodey.server.product.repository.ProductRepository;
import com.foodey.server.product.service.ProductService;
import com.foodey.server.upload.service.CloudinaryService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CloudinaryService cloudinaryService;

  @Override
  @Cacheable(value = "product", key = "#id")
  public Product findById(String id) {
    return productRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
  }

  @Override
  @Cacheable(value = "products", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
  public Slice<Product> findAll(Pageable pageable) {
    return productRepository.findAll(pageable);
  }

  @Override
  @CachePut(value = "product", key = "#product.id")
  @CacheEvict(value = "products", allEntries = true)
  public Product save(Product product) {
    return productRepository.save(product);
  }

  @Override
  @CacheEvict(value = "product", key = "#id")
  public void deleteById(String id) {
    productRepository.deleteById(id);
  }

  @Override
  public List<Product> findAllById(List<String> ids) {
    return productRepository.findAllById(ids);
  }

  @Override
  public Map<String, Object> getImageUploadApiOptions(String productId) {
    Product product = findById(productId);
    return cloudinaryService.getUploadApiOptions(product.getCldImage());
  }
}
