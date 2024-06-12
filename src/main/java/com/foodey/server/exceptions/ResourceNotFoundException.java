package com.foodey.server.exceptions;

import java.lang.reflect.Field;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {

  private final String resourceName;
  private final String fieldName;
  private final Object fieldValue;

  public ResourceNotFoundException(Object resource, Field field)
      throws IllegalArgumentException, IllegalAccessException {
    super(
        HttpStatus.NOT_FOUND,
        String.format(
            "%s not found with %s : '%s'",
            resource.getClass().getSimpleName(), field.getName(), field.get(resource).toString()));
    this.resourceName = resource.getClass().getSimpleName();
    this.fieldName = field.getName();
    this.fieldValue = field.get(resource);
  }

  public ResourceNotFoundException(Object resource, String fieldName, Object fieldValue) {
    super(
        HttpStatus.NOT_FOUND,
        String.format(
            "%s not found with %s : '%s'",
            resource.getClass().getSimpleName(), fieldName, fieldValue));
    this.resourceName = resource.getClass().getSimpleName();
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

  public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
    super(
        HttpStatus.NOT_FOUND,
        String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    this.resourceName = resourceName;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

  public ResourceNotFoundException(
      String message, String resourceName, String fieldName, Object fieldValue) {
    super(HttpStatus.NOT_FOUND, message);
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
