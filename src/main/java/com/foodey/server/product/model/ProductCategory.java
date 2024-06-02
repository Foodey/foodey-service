package com.foodey.server.product.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodey.server.validation.annotation.OptimizedName;
import io.swagger.v3.oas.annotations.media.Schema;
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

  @Id
  @Schema(description = "The unique identifier of the category")
  private String id;

  @Schema(description = "The name of the category")
  @OptimizedName
  private String name;

  @Schema(description = "The image url of the category")
  private String image = "";

  @Schema(description = "The description of the category")
  private String description = "";

  public ProductCategory(String name, String image, String description) {
    this.name = name;
    this.image = image;
    this.description = description;
  }
}
