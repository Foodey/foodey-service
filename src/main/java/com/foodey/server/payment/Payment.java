package com.foodey.server.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Payment */
@Getter
@Setter
@AllArgsConstructor
public class Payment {
  // private PaymentMethod method;
  // private PaymentStatus status;

  private Double price;
}
