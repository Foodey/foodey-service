package com.foodey.server.evaluation.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.experimental.FieldNameConstants;

/** EvaluationType */
@JsonTypeName
@lombok.experimental.FieldNameConstants
public enum EvaluationType {
  @FieldNameConstants.Include
  ORDER,

  @FieldNameConstants.Include
  PRODUCT,
}
