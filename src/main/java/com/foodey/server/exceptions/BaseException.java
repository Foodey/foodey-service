package com.foodey.server.exceptions;

import org.springframework.http.HttpStatus;

/** HttpException */
public abstract class BaseException extends RuntimeException {

  protected HttpStatus status;

  public BaseException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

  public BaseException(HttpStatus status, String message, Throwable cause) {
    super(message, cause);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
