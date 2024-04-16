package com.foodey.server.shop.model;

import com.foodey.server.validation.annotation.OptimizedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "shops")
public class Shop {
  @Id private String id;

  @OptimizedName private String name;

  private String logo;
  private String wallpaper;

  private long rating = 0;

  @NotBlank
  @Size(min = 2, max = 100)
  private String address;

  private List<@Valid ShopMenu> menus = new ArrayList<>();

  @DBRef private ShopBranch branch;

  public Shop(String name, String address, String logo, String wallpaper) {
    this.name = name;
    this.address = address;
    this.logo = logo;
    this.wallpaper = wallpaper;
  }
}
