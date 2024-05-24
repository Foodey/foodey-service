package com.foodey.server.shopcart;

import lombok.experimental.FieldNameConstants;

/** ShopCartAction */
@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum ShopCartProductAction {
  @FieldNameConstants.Include
  ADD_PRODUCT,

  @FieldNameConstants.Include
  REMOVE_PRODUCT,

  @FieldNameConstants.Include
  INCREASE_PRODUCT_QUANTITY,

  @FieldNameConstants.Include
  REPLACE_PRODUCT_QUANTITY,

  @FieldNameConstants.Include
  DECREASE_PRODUCT_QUANTITY,
}
