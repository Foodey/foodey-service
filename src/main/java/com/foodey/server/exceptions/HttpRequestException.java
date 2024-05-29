package com.foodey.server.exceptions;

import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Getter
public class HttpRequestException extends RuntimeException {

  protected HttpStatus responseStatus;
  protected HttpMethod method;

  public HttpRequestException(HttpMethod method, HttpStatus responseStatus, String message) {
    super(
        "Failed to request with method: "
            + method
            + " response status: "
            + responseStatus
            + " response: "
            + message);
    this.method = method;
    this.responseStatus = responseStatus;
  }
}
