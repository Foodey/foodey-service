package com.foodey.server.shop.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodey.server.validation.annotation.OptimizedName;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "shop_menus")
@JsonIgnoreProperties(
    value = {"id", "productIds"},
    allowGetters = true)
public class ShopMenu {

  @Indexed @Id private String id = NanoIdUtils.randomNanoId();

  @OptimizedName private String name;

  private String description = "";

  private Set<String> productIds = new HashSet<>();
}
