package com.foodey.server.shopcart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductQuantity {
  private String productId;
  private Long quantity;
}
