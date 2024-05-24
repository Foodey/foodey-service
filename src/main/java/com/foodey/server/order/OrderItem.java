package com.foodey.server.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItem {
  @NotBlank private String productId;

  private String name;

  private String image;

  private String description;

  private String categoryId;

  private double productPrice;

  private Long quantity;

  private double totalPrice;

  public OrderItem(
      String productId,
      String name,
      String image,
      String description,
      String categoryId,
      double productPrice,
      Long quantity,
      double totalPrice) {
    this.productId = productId;
    this.name = name;
    this.image = image;
    this.description = description;
    this.categoryId = categoryId;
    this.productPrice = productPrice;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
  }
}
