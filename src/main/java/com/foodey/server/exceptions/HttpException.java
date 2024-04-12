package com.foodey.server.exceptions;

import org.springframework.http.HttpStatus;

/** HttpException */
public abstract class HttpException extends RuntimeException {

  protected HttpStatus status;

  public HttpException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

  public HttpException(HttpStatus status, String message, Throwable cause) {
    super(message, cause);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
