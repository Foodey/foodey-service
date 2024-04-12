package com.foodey.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class UserLoginException extends HttpException {

  public UserLoginException(String message) {
    super(HttpStatus.EXPECTATION_FAILED, message);
  }
}
