package com.foodey.server.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

  @NotBlank(message = "Name is mandatory")
  @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
  private String name;

  private String image = "";

  private String description = "";

  @Min(value = 0, message = "Price should be greater than 0")
  @NotNull
  private Long price;

  @Min(value = 0, message = "Rating should be greater than 0")
  private long rating = 0;

  @Min(value = 0, message = "Sold quantity should be greater than 0")
  private long soldQuantity = 0;

  private boolean soldOut = false;

  // @DBRef private ProductCategory category;

  private int menuIndex;

  private String branchId;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;

  public Product() {
    this.rating = 0;
    this.soldQuantity = 0;
    this.soldOut = false;
    this.description = "";
    this.image = "";
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  @Override
  public boolean isNew() {
    return createdAt == null || id == null;
  }
}
