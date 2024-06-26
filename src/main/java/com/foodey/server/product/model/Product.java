package com.foodey.server.product.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.foodey.server.upload.model.CloudinaryImage;
import com.foodey.server.upload.model.CloudinaryImageManager;
import com.foodey.server.validation.annotation.OptimizedName;
import com.mongodb.lang.NonNull;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "products")
@NoArgsConstructor
@JsonIgnoreProperties(
    value = {"id", "ownerId", "createdAt", "updatedAt", "shopId", "brandId"},
    allowGetters = true)
@JsonPropertyOrder("name")
public class Product implements Persistable<String>, CloudinaryImageManager {

  @Schema(description = "The unique identifier of the product")
  @Id
  private String id;

  @Schema(description = "The name of the product")
  @OptimizedName
  private String name;

  @Schema(description = "The unique identifier of the user owner of the product")
  @NonNull
  private String ownerId;

  @Min(0)
  @Schema(description = "The price of the product")
  private double price;

  @JsonInclude(Include.NON_NULL)
  @Transient
  @Schema(description = "This field is just use for payload only")
  private Map<String, Object> imageApiUploadOptions;

  @Schema(description = "The url image of the product")
  private CloudinaryImage cldImage;

  public String getImage() {
    return cldImage != null ? cldImage.getUrl() : "";
  }

  @Schema(description = "The description of the product")
  private String description = "";

  @Schema(description = "The unique identifier of the category that the product belongs to")
  @NotBlank
  private String categoryId;

  @Schema(
      description = "The name of the category that the product belongs to when displayed in menu")
  private String categoryNameDisplayedOnMenu = "-";

  @JsonIgnore private String shopId;

  @JsonIgnore private String brandId;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;

  @JsonCreator
  public Product(
      @JsonProperty("name") String name,
      @JsonProperty("categoryId") String categoryId,
      @JsonProperty("price") long price) {
    this.name = name;
    this.price = price;
    this.categoryId = categoryId;
    this.cldImage = new CloudinaryImage(getCloudinaryFolder(), name);
  }

  public Product(String name, String categoryId, long price, String description) {
    this.name = name;
    this.categoryId = categoryId;
    this.price = price;
    this.description = description;
    this.cldImage = new CloudinaryImage(getCloudinaryFolder(), name);
  }

  public Product(Product product) {
    this.id = product.id;
    this.name = product.name;
    this.ownerId = product.ownerId;
    this.price = product.price;
    this.cldImage = product.cldImage;
    this.description = product.description;
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
