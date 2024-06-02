package com.foodey.server.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodey.server.validation.annotation.OptimizedName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "products")
@NoArgsConstructor
@JsonIgnoreProperties(
    value = {"id", "createdAt", "updatedAt", "shopId", "brandId"},
    allowGetters = true)
public class Product implements Persistable<String> {

  @Schema(description = "The unique identifier of the product")
  @Id
  private String id;

  @Schema(description = "The name of the product")
  @OptimizedName
  private String name;

  @Min(0)
  @Schema(description = "The price of the product")
  private double price;

  @Schema(description = "The image of the product")
  private String image = "";

  @Schema(description = "The description of the product")
  private String description = "";

  @Schema(description = "The unique identifier of the menu that the product belongs to")
  @NotBlank
  private String menuId;

  @Schema(description = "The unique identifier of the category that the product belongs to")
  @NotBlank
  private String categoryId;

  @Schema(
      description = "The name of the category that the product belongs to when displayed in menu")
  private String categoryNameDisplayedOnMenu = "-";

  private String shopId;

  private String brandId;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;

  public Product(String name, long price, String image, String description) {
    this.name = name;
    this.price = price;
    this.image = image;
    this.description = description;
  }

  public Product(Product product) {
    this.id = product.id;
    this.name = product.name;
    this.price = product.price;
    this.image = product.image;
    this.description = product.description;
    this.menuId = product.menuId;
    this.categoryId = product.categoryId;
    this.categoryNameDisplayedOnMenu = product.categoryNameDisplayedOnMenu;
    this.shopId = product.shopId;
    this.brandId = product.brandId;
    this.createdAt = product.createdAt;
    this.updatedAt = product.updatedAt;
  }

  @Override
  @JsonIgnore
  public boolean isNew() {
    return createdAt == null || id == null;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    else if (!(obj instanceof Product)) return false;
    return name.equals(((Product) obj).name) || id.equals(((Product) obj).id);
  }
}
