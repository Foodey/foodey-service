package com.foodey.server.product.service;

import com.foodey.server.product.model.Product;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductService {

  Product findById(String id);

  List<Product> findAllById(List<String> ids);

  Slice<Product> findAll(Pageable pageable);

  Product save(Product product);

  void deleteById(String id);

  Map<String, Object> getImageUploadApiOptions(String productId);
}
