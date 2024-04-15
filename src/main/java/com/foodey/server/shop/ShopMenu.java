package com.foodey.server.shop;

import com.foodey.server.validation.annotation.OptimizedName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "shop_menus")
public class ShopMenu {

  @OptimizedName private String name;

  private String description = "";

  public ShopMenu() {}

  public ShopMenu(String name) {
    this.name = name;
  }
}
