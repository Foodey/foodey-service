package com.foodey.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** TooManyRequestsException */
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class TooManyRequestsException extends HttpException {

  public TooManyRequestsException(String message) {
    super(HttpStatus.TOO_MANY_REQUESTS, message);
  }

  public TooManyRequestsException(String message, Throwable cause) {
    super(HttpStatus.TOO_MANY_REQUESTS, message, cause);
  }
}
