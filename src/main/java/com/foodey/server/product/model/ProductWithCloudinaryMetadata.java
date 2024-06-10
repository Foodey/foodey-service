package com.foodey.server.product.model;

import com.foodey.server.common.model.CloudinaryUploadApiOptionsImpl;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductWithCloudinaryMetadata extends Product {

  public CloudinaryUploadApiOptionsImpl getCloudinaryRequestOptions() {
    return new CloudinaryUploadApiOptionsImpl(getId(), getCloudinaryFolder());
  }

  public ProductWithCloudinaryMetadata(Product product) {
    super(product);
  }
}
