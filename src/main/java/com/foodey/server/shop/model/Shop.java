package com.foodey.server.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.foodey.server.common.model.Address;
import com.foodey.server.upload.model.CloudinaryImage;
import com.foodey.server.upload.model.CloudinaryImageManager;
import com.foodey.server.validation.annotation.OptimizedName;
import com.mongodb.lang.NonNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "shops")
@JsonIgnoreProperties(
    value = {
      "id",
      "ownerId",
      "createdAt",
      "updatedAt",
      "rating",
      "categoryIds, lastRatingCalculationAt"
    },
    allowGetters = true)
@ToString
@NoArgsConstructor
@CompoundIndex(def = "{'name': 1, 'brandId': 1}", unique = true, name = "name_brand_compound_idx")
public class Shop implements Persistable<String>, CloudinaryImageManager {

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

  @Transient
  @JsonInclude(Include.NON_NULL)
  private Map<String, Object> logoUploadApiOptions;

  private CloudinaryImage cldLogo = new CloudinaryImage(getCloudinaryFolder() + "/logos");

  @Schema(description = "The url of logo of the shop")
  public String getLogo() {
    return cldLogo != null ? cldLogo.getUrl() : "";
  }

  @Transient
  @JsonInclude(Include.NON_NULL)
  private Map<String, Object> wallpaperUploadApiOptions;

  @Schema(description = "The url of wallpaper of the shop")
  private CloudinaryImage cldWallpaper = new CloudinaryImage(getCloudinaryFolder() + "/wallpapers");

  public String getWallpaper() {
    return cldWallpaper != null ? cldWallpaper.getUrl() : "";
  }

  @Schema(description = "The address of the shop")
  private Address address;

  @Indexed(name = "shop_categories")
  @Schema(description = "The category ids of the shop ")
  private Set<String> categoryIds = new HashSet<>();

  // Computed Pattern
  @Schema(description = "The rating of the shop")
  private double rating = -1; // -1 means the rating has not been calculated yet

  @JsonIgnore
  public boolean isRatingCaculatedAtLeastOneTime() {
    return rating >= 0;
  }

  /**
   * Get the real rating of the shop, even if it has not been calculated
   *
   * @return
   */
  @JsonIgnore
  public double getRealRating() {
    return rating;
  }

  /**
   * Get the rating of the shop if it has been calculated, otherwise return 0 This feild is used to
   * avoid returning -1 to the client
   *
   * @return
   */
  public double getRating() {
    return rating < 0 ? 0 : rating;
  }

  @Setter(AccessLevel.NONE)
  private Instant lastRatingCalculationAt = Instant.now();

  public void setRating(double rating) {
    assert rating >= 0 && rating <= 5;
    this.rating = rating;
    this.lastRatingCalculationAt = Instant.now();
  }

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;

  public Shop(String name, Address address, String brandId, String ownerId) {
    this.name = name;
    this.address = address;
    this.brandId = brandId;
    this.ownerId = ownerId;
  }

  @Override
  @JsonIgnore
  public boolean isNew() {
    return createdAt == null || id == null;
  }
}
