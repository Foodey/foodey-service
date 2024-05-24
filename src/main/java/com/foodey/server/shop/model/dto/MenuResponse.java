package com.foodey.server.shop.model.dto;

import com.foodey.server.product.model.Product;
import com.foodey.server.shop.model.ShopMenu;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuResponse {

  private String id;

  private String name;

  private String description;

  private List<Product> products;

  public MenuResponse(ShopMenu shopMenu, List<Product> products) {
    this.id = shopMenu.getId();
    this.name = shopMenu.getName();
    this.description = shopMenu.getDescription();
    this.products = products;
  }
}
