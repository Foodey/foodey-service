package com.foodey.server.shopcart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodey.server.order.OrderItem;
import com.foodey.server.voucher.Voucher;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShopCartDetail {

  private double totalRealPrice;
  private double priceAfterDiscount;

  @JsonIgnore
  @Setter(AccessLevel.NONE)
  private ShopCart shopCart;

  public long getNumberOfItems() {
    return shopCart.getNumberOfItems();
  }

  public Voucher getVoucher() {
    return shopCart.getVoucher();
  }

  private List<OrderItem> items;

  public ShopCartDetail(ShopCart shopCart, List<OrderItem> items) {
    this.shopCart = shopCart;
    this.items = items;
    caculatePrice(items);
  }

  void caculatePrice(List<OrderItem> items) {
    totalRealPrice = items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    if (getVoucher() != null)
      priceAfterDiscount = getVoucher().getPriceAfterDiscount(totalRealPrice, items);
    else priceAfterDiscount = totalRealPrice;
  }

  public double getTotalRealPrice() {
    return totalRealPrice;
  }

  public double getTotalDiscount() {
    return totalRealPrice - priceAfterDiscount;
  }

  public double getPriceAfterDiscount() {
    return priceAfterDiscount;
  }
}
