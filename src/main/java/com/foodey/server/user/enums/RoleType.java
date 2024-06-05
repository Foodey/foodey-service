package com.foodey.server.user.enums;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public enum RoleType {
  @FieldNameConstants.Include
  ADMIN,

  @FieldNameConstants.Include
  CUSTOMER,

  @FieldNameConstants.Include
  SELLER,

  @FieldNameConstants.Include
  SHIPPER,
  ;
}
