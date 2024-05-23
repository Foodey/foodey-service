package com.foodey.server.shop.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopMenuFound {

  private boolean global;

  private ShopMenu value;

  public ShopMenuFound(boolean global, ShopMenu value) {
    this.global = global;
    this.value = value;
  }
}
