package com.foodey.server.shopcart;

import com.foodey.server.order.OrderItem;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShopCartDetail {

  private double totalPrice;
  private long numberOfItems;

  private List<OrderItem> items;

  public ShopCartDetail(ShopCart shopCart, List<OrderItem> items) {
    this.totalPrice = shopCart.getTotalPrice();
    this.numberOfItems = shopCart.getNumberOfItems();
    this.items = items;
  }
}
