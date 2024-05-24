package com.foodey.server.voucher;

import com.foodey.server.order.OrderItem;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Transient;

/** Vouncher */
@Getter
@Setter
@AllArgsConstructor
@Builder
@Document(collection = "vouchers")
// @ValidVoucher
public class Voucher {

  @Transient
  @Getter
  @Setter
  public static final class Result {

    private Double newTotalMoney;
    // If this voucher is PRODUCT type,
    // then this list will contain the product ids that can be used to donate to the customer
    // And the first element is the quantity of products can be donated
    private Object[] donateProducts = {0, null};
    private String id;
    private String code;
    private String name;
    private String image;
    private String description;
    private VoucherType type;
    private VoucherMethod method;
    private Double discountAmount;

    public Result(Double newTotalMoney, String voucherId, String voucherCode) {
      this.newTotalMoney = newTotalMoney;
      this.id = voucherId;
      this.code = voucherCode;
    }

    public Result(
        Double newTotalMoney, Object[] donateProducts, String voucherId, String voucherCode) {
      this.newTotalMoney = newTotalMoney;
      this.donateProducts = donateProducts;
      this.id = voucherId;
      this.code = voucherCode;
    }

    public Result(
        Double newTotalMoney,
        Object[] donateProducts,
        String voucherId,
        String voucherCode,
        String voucherName,
        String voucherImage,
        String voucherDescription,
        VoucherType voucherType,
        VoucherMethod voucherMethod,
        Double voucherDiscountAmount) {
      this.newTotalMoney = newTotalMoney;
      this.donateProducts = donateProducts;
      this.id = voucherId;
      this.code = voucherCode;
      this.name = voucherName;
      this.image = voucherImage;
      this.description = voucherDescription;
      this.type = voucherType;
      this.method = voucherMethod;
      this.discountAmount = voucherDiscountAmount;
    }
  }

  @Id private String id;

  private String code;

  @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
  @NotBlank
  private String name;

  @lombok.Builder.Default private String image = "";
  @lombok.Builder.Default private String description = "";

  @NotNull private VoucherType type;
  @NotNull private VoucherMethod method;

  // If this voucher created by a branch else null
  private String fromBranchId;

  @NotNull @FutureOrPresent @DateTimeFormat private Instant activationDate;

  @NotNull @DateTimeFormat @Future private Instant expiryDate;

  // If this voucher is only for unique customer else null
  private List<String> customerIds;
  // If this list is empty or null, then this voucher can be used in all restaurants
  private List<String> appliedStoreId;
  // If this list is empty or null, then this voucher can be used in all categories
  private List<String> appliedCategoriesId;
  // If this list is empty or null, then this voucher can be used in all products
  private List<String> appliedProductsId;

  // The maximum number of times this voucher can be used
  @lombok.Builder.Default private Long usageLimitTimes = 1L;

  // Minimum quantity of products have to buy for the voucher to apply
  @lombok.Builder.Default private Long minimumBuyingQuantity = 1L;

  // Minimum quantity of products have to buy for the voucher to apply
  private Long minimumDistance;

  @lombok.Builder.Default private Instant createdAt = Instant.now();
  @lombok.Builder.Default private Instant updatedAt = Instant.now();

  // Amount discount rely on the voucher method
  // If the voucher method is SPECIAL_AMOUNT, then the discount amount is the specific money amount
  // or specific product amount if type is PRODUCT
  // If the voucher method is PERCENTAGE, then the discount amount is the percentage of the total
  @NotNull private Double discountAmount;

  public Voucher() {
    this.usageLimitTimes = 1L;
    this.minimumBuyingQuantity = 1L;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
    this.description = "";
    this.image = "";
  }

  public boolean isExpired() {
    return this.expiryDate.isBefore(Instant.now());
  }

  public boolean isActivated() {
    return this.activationDate.isBefore(Instant.now());
  }

  public boolean isApplicableToStore(String storeId) {
    return this.appliedStoreId == null
        || this.appliedStoreId.isEmpty()
        || this.appliedStoreId.contains(storeId);
  }

  public boolean isApplicableToCategory(String categoryId) {
    return this.appliedCategoriesId == null
        || this.appliedCategoriesId.isEmpty()
        || this.appliedCategoriesId.contains(categoryId);
  }

  public boolean isApplicableToProduct(String productId) {
    return this.appliedProductsId == null
        || this.appliedProductsId.isEmpty()
        || this.appliedProductsId.contains(productId);
  }

  public boolean isApplicableToCustomer(String customerId) {
    return this.customerIds == null
        || this.customerIds.isEmpty()
        || this.customerIds.contains(customerId);
  }

  public boolean isExceedUsageLimitTimes() {
    return this.usageLimitTimes < 1;
  }

  public boolean isEnoughMiniumDistance(Long distance) {
    return distance == null || !(distance < this.minimumDistance);
  }

  public boolean isEnoughMiniumBuyingQuantity(Long quantity) {
    return quantity >= this.minimumBuyingQuantity;
  }

  public boolean isEnoughMiniumBuyingQuantityOfCategory(List<OrderItem> boughtOrderItems) {
    return this.isEnoughMiniumBuyingQuantity(
        boughtOrderItems.stream()
            .filter(
                orderItem ->
                    isApplicableToProduct(orderItem.getProductId())
                        && isApplicableToCategory(orderItem.getCategoryId()))
            .count());
  }

  public boolean canBeUsed(List<OrderItem> boughtOrderItems, String storeId, String customerId) {

    return this.isApplicableToCustomer(customerId)
        && !this.isExpired()
        && this.isActivated()
        && !this.isExceedUsageLimitTimes()
        && this.isApplicableToStore(storeId)
        && this.isEnoughMiniumBuyingQuantityOfCategory(boughtOrderItems);
  }

  public boolean canBeUsed(
      List<OrderItem> boughtOrderItems, String storeId, String customerId, Long distance) {

    return this.isApplicableToCustomer(customerId)
        && !this.isExpired()
        && this.isActivated()
        && !this.isExceedUsageLimitTimes()
        && this.isApplicableToStore(storeId)
        && this.isEnoughMiniumBuyingQuantityOfCategory(boughtOrderItems)
        && this.isEnoughMiniumDistance(distance);
  }

  public Double calcMoneyAfterDiscount(Double moneyBeforeDiscount, VoucherMethod method) {
    switch (method) {
      case PERCENTAGE:
        return moneyBeforeDiscount * (1 - this.discountAmount / 100);
      case SPECIAL_AMOUNT:
        return moneyBeforeDiscount - this.discountAmount;
      default:
        throw new IllegalStateException("Unexpected value: " + method);
    }
  }

  public Result useVoucher(
      List<OrderItem> boughtOrderItems,
      String storeId,
      String customerId,
      VoucherType type,
      VoucherMethod method,
      Double totalMoneyBeforeDiscount) {

    if (!this.canBeUsed(boughtOrderItems, storeId, customerId)) return null;

    switch (type) {
      case MONEY:
      case DELIVERY:
        return new Result(
            calcMoneyAfterDiscount(totalMoneyBeforeDiscount, method),
            null,
            this.id,
            this.code,
            this.name,
            this.image,
            this.description,
            this.type,
            this.method,
            this.discountAmount);
      case PRODUCT:
        return new Result(
            totalMoneyBeforeDiscount,
            new Object[] {
              this.discountAmount, this.appliedProductsId,
            },
            this.id,
            this.code,
            this.name,
            this.image,
            this.description,
            this.type,
            this.method,
            this.discountAmount);
      default:
        return null;
    }
  }

  public void generateCode() {
    this.code = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
  }

  public void generateCode(Function<String, Boolean> isCodeExist) {
    {
      if (isCodeExist == null) {
        throw new IllegalArgumentException("isCodeExist cannot be null");
      }
      byte tryCount = 0;
      while (tryCount++ < 10) {
        generateCode();
        if (!isCodeExist.apply(this.code)) {
          return;
        }
      }

      // throw new OutOfLimitException("Try to generate voucher code too many times");
    }
  }
}
