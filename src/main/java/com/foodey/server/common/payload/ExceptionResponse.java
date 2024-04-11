package com.foodey.server.common.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse extends ApiResponse {
  private String message;

  public ExceptionResponse(HttpStatus status, String message, String path) {
    this(status, message, null, path);
  }

  public ExceptionResponse(HttpStatus status, String message, Object data, String path) {
    this(status, message, data, path, null);
  }

  public ExceptionResponse(
      HttpStatus status, String message, Object data, String path, String cause) {
    super(status, data, path, cause);
    this.message = message;
  }

  public ExceptionResponse(
      HttpStatus status, String message, Object data, HttpServletRequest request, String cause) {
    super(status, data, request, cause);
    this.message = message;
  }

  public ExceptionResponse(
      HttpStatus status, String message, Object data, HttpServletRequest request) {
    this(status, message, data, request, null);
  }

  public ExceptionResponse(HttpStatus status, String message, HttpServletRequest request) {
    this(status, message, null, request);
  }
}
