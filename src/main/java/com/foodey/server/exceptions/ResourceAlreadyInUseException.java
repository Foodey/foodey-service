package com.foodey.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyInUseException extends HttpException {

  private final String resourceName;
  private final String fieldName;
  private final transient Object fieldValue;

  public ResourceAlreadyInUseException(String resourceName, String fieldName, Object fieldValue) {
    super(
        HttpStatus.CONFLICT,
        String.format("%s already in use with %s : '%s'", resourceName, fieldName, fieldValue));
    this.resourceName = resourceName;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

  public ResourceAlreadyInUseException(
      String message, String resourceName, String fieldName, Object fieldValue) {
    super(HttpStatus.CONFLICT, message);
    this.resourceName = resourceName;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

  public String getResourceName() {
    return resourceName;
  }

  public String getFieldName() {
    return fieldName;
  }

  public Object getFieldValue() {
    return fieldValue;
  }
}
