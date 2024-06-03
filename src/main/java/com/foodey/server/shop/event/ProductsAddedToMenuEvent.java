package com.foodey.server.shop.event;

import com.foodey.server.product.model.Product;
import com.foodey.server.user.model.User;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ProductsAddedToMenuEvent extends ApplicationEvent {
  private User user;
  private String brandId;
  private String shopId;
  private List<Product> products;
  private boolean appliedToAllShop;

  public ProductsAddedToMenuEvent(
      Object source,
      User user,
      String brandId,
      String shopId,
      List<Product> products,
      boolean appliedToAllShop) {
    super(source);
    this.user = user;
    this.brandId = brandId;
    this.shopId = shopId;
    this.products = products;
    this.appliedToAllShop = appliedToAllShop;
  }
}
