package com.foodey.server.voucher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodey.server.shopcart.ShopCartDetail;
import com.foodey.server.validation.annotation.OptimizedName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

/** Vouncher */
@Getter
@Setter
@AllArgsConstructor
@Builder
@Document(collection = "vouchers")
@JsonIgnoreProperties(
    value = {
      "id",
      "createdAt",
      "updatedAt",
      "shopOrBrandId",
    },
    allowGetters = true)
public class Voucher implements Persistable<String> {

  @Id
  @Schema(description = "The voucher id")
  private String id;

  private String code;

  @Schema(description = "The name of the voucher")
  @OptimizedName
  private String name;

  @Default private String image = "";

  @Default private String description = "";

  @Schema(description = "The type of the voucher")
  @NotNull
  private VoucherType type;

  @Schema(description = "The method of the voucher")
  @NotNull
  private VoucherMethod method;

  @Schema(
      description =
          "The date when the voucher is activated, the voucher can be used after this date, default"
              + " is now. The seller can set this date to the future to activate the voucher"
              + " later.")
  @Default
  @FutureOrPresent
  @DateTimeFormat
  private Instant activationDate = Instant.now();

  @Schema(
      description =
          "The date when the voucher is expired, the voucher cannot be used after this date.")
  @NotNull
  @Future
  @DateTimeFormat
  private Instant expiryDate;

  @CreatedDate
  @Schema(description = "The time the voucher is created")
  private Instant createdAt;

  @LastModifiedDate
  @Schema(description = "The last time the voucher is updated")
  private Instant updatedAt;

  // constraints

  @Schema(description = "The shop or brand id that this voucher can be used. If null, then all")
  private String shopVsBrandId;

  @Schema(description = "The list of category ids that this voucher can be used")
  private Set<String> appliedCategoryIds;

  @Schema(description = "The list of product ids that this voucher can be used")
  private Set<String> appliedProductIds;

  @Default
  @Schema(
      description =
          "The number of times this voucher can be used, When this number is 0, the"
              + " voucher cannot be used anymore.")
  private Long quantity = 1L;

  @Schema(description = "The minimum quantity of products have to buy for the voucher to apply")
  @Default
  private Long minimumBuyingQuantity = 1L;

  @Schema(
      description = "The minimum distance from the store for the voucher to apply. The unit is km")
  @Default
  private Long minimumDistanceFromStore = 5L;

  @NotNull
  @Min(0)
  @Schema(
      description =
          "The discount amount of the voucher. The unit is percentage or money or product. If the"
              + " method is PERCENTAGE, then the discount amount is the percentage of the total. If"
              + " the method is SPECIAL_AMOUNT, then the discount amount is the specific money"
              + " amount or specific product amount if type is PRODUCT.")
  private Double discountAmount;

  public boolean isExpired() {
    return this.expiryDate.isBefore(Instant.now());
  }

  public boolean isActivated() {
    return this.activationDate.isBefore(Instant.now());
  }

  private boolean isApplicableToStore(String storeId) {
    return !StringUtils.hasText(this.shopVsBrandId) || this.shopVsBrandId.equals(storeId);
  }

  public boolean isApplicableToStore(String storeId, String brandId) {
    return isApplicableToStore(storeId) || isApplicableToStore(brandId);
  }

  public boolean isApplicableToCategory(String categoryId) {
    return appliedCategoryIds.contains(categoryId);
  }

  public boolean isApplicableToProduct(String productId) {
    return appliedProductIds.contains(productId);
  }

  public boolean isEnoughMiniumDistance(Long distance) {
    return distance >= this.minimumDistanceFromStore;
  }

  public boolean isEnoughMiniumBuyingQuantity(Long quantity) {
    return quantity >= this.minimumBuyingQuantity;
  }

  public boolean canBeUsed() {
    return !isExpired() && isActivated() && quantity > 0;
  }

  public boolean canBeAppliedTo(String storeId, String brandId, ShopCartDetail shopCartDetail) {
    return canBeUsed();
  }

  @Override
  @JsonIgnore
  public boolean isNew() {
    return createdAt == null || id == null;
  }
}
