package com.foodey.server.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodey.server.validation.annotation.OptimizedName;
import com.mongodb.lang.NonNull;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "shops")
@JsonIgnoreProperties(
    value = {"id", "ownerId", "createdAt", "updatedAt", "rating", "categoryIds"},
    allowGetters = true)
@CompoundIndex(def = "{'name': 1, 'brandId': 1}", name = "shop_name_brand_id")
@ToString
@NoArgsConstructor
public class Shop implements Persistable<String> {

  @Schema(description = "The unique identifier of the shop")
  @Id
  private String id;

  @Schema(description = "The unique identifier of the brand that the shop belongs to")
  @NonNull
  private String brandId;

  @Schema(description = "The unique identifier of the owner of the shop")
  @NonNull
  private String ownerId;

  @Schema(description = "The name of the shop")
  @OptimizedName
  private String name;

  @Schema(description = "The url of logo of the shop")
  private String logo;

  @Schema(description = "The url of wallpaper of the shop")
  private String wallpaper;

  @NotBlank
  @Size(min = 2, max = 100)
  @Schema(description = "The address of the shop")
  private String address;

  @Indexed(name = "shop_categories")
  @Schema(description = "The category ids of the shop ")
  private Set<String> categoryIds = new HashSet<>();

  @Schema(description = "The rating of the shop")
  private double rating = -1; // -1 means the rating has not been calculated yet

  @Setter(AccessLevel.NONE)
  private Instant lastRatingCalculationAt = Instant.now();

  public void setRating(double rating) {
    this.rating = rating;
    this.lastRatingCalculationAt = Instant.now();
  }

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;

  public Shop(
      String name, String address, String logo, String wallpaper, String brandId, String ownerId) {
    this.name = name;
    this.address = address;
    this.logo = logo;
    this.wallpaper = wallpaper;
    this.brandId = brandId;
  }

  @Override
  @JsonIgnore
  public boolean isNew() {
    return createdAt == null || id == null;
  }
}
