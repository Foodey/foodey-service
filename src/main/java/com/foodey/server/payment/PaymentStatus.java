package com.foodey.server.payment;

/** PaymentStatus */
public enum PaymentStatus {
  UNPAID,
  PENDING,
  COMPLETED,
  FAILED,
  DECLINED,
  CANCELED,
  ABANDONED,
  SETTING,
  SETTLED,
  REFUNDED,
  UNKNOWN,
}
