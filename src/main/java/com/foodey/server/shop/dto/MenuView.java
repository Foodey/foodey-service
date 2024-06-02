package com.foodey.server.shop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.foodey.server.product.model.Product;
import java.util.Collection;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuView {

  String currentCategoryName;

  Set<String> categoryNames;

  Collection<Product> products;

  int numberOfProducts;
}
