package com.foodey.server.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodey.server.validation.annotation.OptimizedName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
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
    value = {"id", "createdAt", "updatedAt", "rating", "soldQuantity", "soldOut"},
    allowGetters = true)
public class Product implements Persistable<String> {

  @Null @Id private String id;

  @OptimizedName private String name;

  @Min(0)
  private double price;

  private String image = "";

  private String description = "";

  @NotBlank private String menuId;

  @NotBlank private String shopId;

  @NotBlank private String categoryId;

  private long rating = 0;

  private long soldQuantity = 0;

  private boolean soldOut = false;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;

  public Product(String name, long price, String image, String description) {
    this.name = name;
    this.price = price;
    this.image = image;
    this.description = description;
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
