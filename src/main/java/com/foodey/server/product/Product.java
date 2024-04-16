package com.foodey.server.product;

import com.foodey.server.validation.annotation.OptimizedName;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

/** Product */
@Getter
@Setter
@Document(collection = "products")
public class Product implements Persistable<String> {

  @Id private String id;

  @OptimizedName private String name;

  private long price;

  private String image;

  private String description;

  private int menuIndex;

  private String branchId;

  private ProductCategory category;

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
  public boolean isNew() {
    return createdAt == null || id == null;
  }
}
