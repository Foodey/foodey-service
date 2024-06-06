package com.foodey.server.common.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.foodey.server.exceptions.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse extends ApiResponse {
  private String message;

  public ExceptionResponse(
      BaseException ex, String message, Object payload, HttpServletRequest request) {
    super(ex.getStatus(), payload, request, ex.getClass());
    this.message = message;
  }

  public ExceptionResponse(BaseException ex, Object payload, HttpServletRequest request) {
    this(ex, ex.getMessage(), payload, request);
  }

  public ExceptionResponse(BaseException ex, String message, HttpServletRequest request) {
    this(ex, message, null, request);
  }

  public ExceptionResponse(BaseException ex, HttpServletRequest request) {
    this(ex, ex.getMessage(), null, request);
  }

  public ExceptionResponse(
      Exception ex, HttpStatus status, String message, Object payload, HttpServletRequest request) {
    super(status, payload, request, ex.getClass());
    this.message = message;
  }

  public ExceptionResponse(
      Exception ex, HttpStatus status, Object payload, HttpServletRequest request) {
    this(ex, status, ex.getMessage(), payload, request);
  }
}
