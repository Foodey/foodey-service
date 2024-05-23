package com.foodey.server.shop.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopMenuFound {

  private ShopMenusContainer from;

  private ShopMenu value;

  public ShopMenuFound(ShopMenusContainer from, ShopMenu value) {
    this.from = from;
    this.value = value;
  }
}
