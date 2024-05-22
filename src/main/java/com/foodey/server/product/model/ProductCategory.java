package com.foodey.server.product.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodey.server.validation.annotation.OptimizedName;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "product_categories")
@JsonIgnoreProperties(
    value = {"id"},
    allowGetters = true)
@NoArgsConstructor
public class ProductCategory {

  @Null @Id private String id;

  @OptimizedName private String name;

  private String image = "";

  private String description = "";

  public ProductCategory(String name, String image, String description) {
    this.name = name;
    this.image = image;
    this.description = description;
  }
}
