package com.foodey.server.common.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ApiResponse {

  private int code;
  private final Object data;
  private final String timestamp;
  private final String cause;
  private final String path;
  private HttpStatus status;

  public ApiResponse(HttpStatus status, Object data, String path, String cause) {
    this.status = status;
    this.data = data;
    this.path = path;
    this.cause = cause;
    this.timestamp = Instant.now().toString();
    this.code = status.value();
  }

  public ApiResponse(HttpStatus status, Object data, String path) {
    this(status, data, path, null);
  }

  public ApiResponse(HttpStatus status, String path) {
    this(status, null, path, null);
  }

  public ApiResponse(HttpStatus status, Object data, HttpServletRequest request, String cause) {
    this.status = status;
    this.data = data;
    this.path = request.getServletPath();
    this.cause = cause;
    this.timestamp = Instant.now().toString();
    this.code = status.value();
  }

  public ApiResponse(HttpStatus status, Object data, HttpServletRequest request) {
    this(status, data, request, null);
  }

  public ApiResponse(HttpStatus status, HttpServletRequest request) {
    this(status, null, request, null);
  }

  public ResponseEntity<ApiResponse> toResponseEntity() {
    return new ResponseEntity<>(this, this.status);
  }
}
