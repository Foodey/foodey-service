package com.foodey.server.evaluation.model;

import lombok.experimental.FieldNameConstants;

/** EvaluationType */
@lombok.experimental.FieldNameConstants
public enum EvaluationType {
  @FieldNameConstants.Include
  ORDER,

  @FieldNameConstants.Include
  PRODUCT,

  @FieldNameConstants.Include
  SHIPPER,
}
