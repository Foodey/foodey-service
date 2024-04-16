package com.foodey.server.product;

import com.foodey.server.validation.annotation.OptimizedName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "product_categories")
public class ProductCategory {

  @Id private String id;

  @OptimizedName private String name;

  private String image;

  private String description;

  public ProductCategory(String name, String image, String description) {
    this.name = name;
    this.image = image;
    this.description = description;
  }
}
