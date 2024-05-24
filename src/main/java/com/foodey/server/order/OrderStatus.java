package com.foodey.server.order;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum OrderStatus {
  @FieldNameConstants.Include
  PENDING,

  @FieldNameConstants.Include
  SHIPPER_RECEIVED,

  @FieldNameConstants.Include
  NO_SHIPPER_FOUND,

  @FieldNameConstants.Include
  STORE_CONFIRMED,

  @FieldNameConstants.Include
  DELIVERING,

  @FieldNameConstants.Include
  DELIVERED,

  @FieldNameConstants.Include
  CANCELED,

  @FieldNameConstants.Include
  UNKNOWN,
  ;
}
