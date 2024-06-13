package com.foodey.server.product.model;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductWithApiUploadOptions extends Product {

  Map<String, Object> imageApiUploadOptions;

  public ProductWithApiUploadOptions(Product product, Map<String, Object> imageApiUploadOptions) {
    super(product);
    this.imageApiUploadOptions = imageApiUploadOptions;
  }
}
