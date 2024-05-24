package com.foodey.server.order;

public enum OrderStatus {
  PENDING,
  SHIPPER_RECEIVED,
  NO_SHIPPER_FOUND,
  STORE_CONFIRMED,
  DELIVERING,
  DELIVERED,
  CANCELED,
  UNKNOWN,
  ;
}
