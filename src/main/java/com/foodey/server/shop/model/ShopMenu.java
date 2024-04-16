package com.foodey.server.shop.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.foodey.server.validation.annotation.OptimizedName;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "shop_menus")
public class ShopMenu {

  @Id private String id;

  @OptimizedName private String name;

  private String description;

  private List<String> productIds = new ArrayList<>();

  public ShopMenu(String name, String description) {
    this.id = NanoIdUtils.randomNanoId();
    this.name = name;
    this.description = description != null ? description : "";
  }
}
