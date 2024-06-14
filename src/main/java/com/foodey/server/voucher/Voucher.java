package com.foodey.server.voucher;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodey.server.order.OrderItem;
import com.foodey.server.upload.model.CloudinaryImage;
import com.foodey.server.validation.annotation.OptimizedName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "vouchers")
@JsonIgnoreProperties(
    value = {"id", "createdAt", "updatedAt", "createdBy"},
    allowGetters = true)
public class Voucher implements Persistable<String> {

  @Id
  @Schema(description = "The voucher id")
  private String id;

  @Indexed(unique = true)
  private String code;

  @JsonProperty("code")
  public void setCode(String code) {
    if (!StringUtils.hasText(code)) {
      this.code = NanoIdUtils.randomNanoId();
    } else {
      this.code = code;
    }
  }

  // private String generateUniqueCode() {
  //   char[] alphabet =
  //       "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
  //   return NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, alphabet, 10);
  // }

  @Schema(description = "The name of the voucher")
  @OptimizedName
  private String name;

  @JsonInclude(Include.NON_NULL)
  @Transient
  @Schema(description = "This field is just use for payload only")
  private Map<String, Object> imageApiUploadOptions;

  @Schema(description = "The url image of the product")
  @Default
  private CloudinaryImage cldImage = new CloudinaryImage();

  public String getImage() {
    return cldImage != null ? cldImage.getUrl() : "";
  }

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
  private Instant activationDate = Instant.now().plus(Duration.ofMinutes(3));

  @JsonProperty("activationDate")
  public void setActivationDate(@FutureOrPresent @DateTimeFormat Instant activationDate) {
    this.activationDate =
        (activationDate == null) ? Instant.now().plus(Duration.ofMinutes(3)) : activationDate;
  }

  @Schema(
      description =
          "The date when the voucher is expired, the voucher cannot be used after this date.")
  // @NotNull
  @Future
  @DateTimeFormat
  @Default
  private Instant expiryDate = Instant.now().plus(Duration.ofDays(7));

  @JsonProperty("expiryDate")
  public void setExpiryDate(@Future @DateTimeFormat Instant expiryDate) {
    this.expiryDate = (expiryDate == null) ? Instant.now().plus(Duration.ofDays(7)) : expiryDate;
  }

  @CreatedDate
  @Schema(description = "The time the voucher is created")
  @JsonIgnore
  private Instant createdAt;

  @LastModifiedDate
  @Schema(description = "The last time the voucher is updated")
  @JsonIgnore
  private Instant updatedAt;

  @Schema(description = "The user id who created this voucher")
  @CreatedBy
  @JsonIgnore
  private String createdBy;

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
  private int minimumBuyingQuantity = 1;

  @Schema(
      description = "The minimum distance from the store for the voucher to apply. The unit is km")
  @Default
  private int minimumDistanceFromStore = 5;

  @Min(0)
  @Schema(
      description =
          "The discount amount of the voucher. The unit is percentage or money or product. If the"
              + " method is PERCENTAGE, then the discount amount is the percentage of the total. If"
              + " the method is SPECIAL_AMOUNT, then the discount amount is the specific money"
              + " amount or specific product amount if type is PRODUCT.")
  @NotNull
  private Double discountAmount;

  public Double getPriceAfterDiscount(List<OrderItem> orderItems) {
    Double currentPrice = orderItems.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    return getPriceAfterDiscount(currentPrice, orderItems);
  }

  public Double getPriceAfterDiscount(Double currentPrice, List<OrderItem> orderItems) {
    switch (method) {
      case PERCENTAGE:
        return currentPrice * (100 - discountAmount) / 100;
      case SPECIAL_AMOUNT:
        switch (type) {
          case MONEY:
            {
              double newPrice = currentPrice - discountAmount;
              return newPrice < 0 ? 0 : newPrice;
            }
          case PRODUCT:
            {
              // filter items that can be apply and sort by price
              boolean hasProductConstraint =
                  appliedProductIds != null && !appliedProductIds.isEmpty();
              boolean hasCategoryConstraint =
                  appliedCategoryIds != null && !appliedCategoryIds.isEmpty();

              List<OrderItem> itemsApllicaple =
                  orderItems.stream()
                      .filter(
                          item -> {
                            if (hasProductConstraint
                                && appliedProductIds.contains(item.getProductId())) {
                              return true;
                            } else if (hasCategoryConstraint
                                && appliedCategoryIds.contains(item.getCategoryId())) {
                              return true;
                            }
                            return !hasProductConstraint && !hasCategoryConstraint; // no constraint
                          })
                      .sorted((a, b) -> Double.compare(a.getTotalPrice(), b.getTotalPrice()))
                      .toList();

              Double discountedAmount = discountAmount;
              double newPrice = currentPrice;
              for (OrderItem item : itemsApllicaple) {
                if (discountedAmount >= item.getTotalPrice()) {
                  discountedAmount -= item.getQuantity();
                  newPrice -= item.getTotalPrice();
                } else {
                  newPrice -= discountedAmount * item.getProductPrice();
                  break;
                }
              }
              return newPrice;
            }
          default:
            break;
        }
    }
    return currentPrice;
  }

  public Voucher apply() {
    if (this.quantity > 0) {
      this.quantity--;
    }
    return this;
  }

  public boolean isExpired() {
    return this.expiryDate.isBefore(Instant.now());
  }

  public boolean isActivated() {
    return this.activationDate.isBefore(Instant.now());
  }

  public boolean isEnoughQuantity() {
    return this.quantity > 0;
  }

  private boolean isApplicableToStore(String storeId) {
    return !StringUtils.hasText(this.shopVsBrandId) || this.shopVsBrandId.equals(storeId);
  }

  public boolean isApplicableToStore(List<String> shopVsBrandIds) {
    return shopVsBrandIds.stream().anyMatch(this::isApplicableToStore);
  }

  public boolean isApplicableToCategory(String categoryId) {
    return appliedCategoryIds == null || appliedCategoryIds.contains(categoryId);
  }

  public boolean isApplicableToProduct(String productId) {
    return appliedProductIds == null || appliedProductIds.contains(productId);
  }

  public boolean isEnoughMiniumDistance(int distance) {
    return distance >= this.minimumDistanceFromStore;
  }

  public boolean isEnoughMiniumBuyingQuantity(List<OrderItem> orderItems) {
    if (orderItems == null || orderItems.isEmpty()) return false;

    long totalConstraintQuantity = 0;
    long totalQuantity = 0;

    boolean hasProductConstraint = appliedProductIds != null && !appliedProductIds.isEmpty();
    boolean hasCategoryConstraint = appliedCategoryIds != null && !appliedCategoryIds.isEmpty();

    for (OrderItem orderItem : orderItems) {
      totalQuantity += orderItem.getQuantity();
      if (hasProductConstraint && appliedProductIds.contains(orderItem.getProductId())) {
        totalConstraintQuantity += orderItem.getQuantity();
      } else if (hasCategoryConstraint && appliedCategoryIds.contains(orderItem.getCategoryId())) {
        totalConstraintQuantity += orderItem.getQuantity();
      }
      if (totalConstraintQuantity > minimumBuyingQuantity) return true;
    }
    if (hasProductConstraint || hasCategoryConstraint) return false;
    return totalQuantity >= minimumBuyingQuantity;
  }

  public boolean canBeUsed() {
    return !isExpired() && isActivated() && quantity > 0;
  }

  @Override
  @JsonIgnore
  public boolean isNew() {
    return createdAt == null || id == null;
  }
}
