package com.foodey.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class AccountRegistrationException extends HttpException {

  private final String username;

  public AccountRegistrationException(String username, String message) {
    super(
        HttpStatus.EXPECTATION_FAILED,
        String.format("Failed to register account[%s] : '%s'", username, message));
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
