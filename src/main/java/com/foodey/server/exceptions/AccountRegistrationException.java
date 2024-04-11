package com.foodey.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class AccountRegistrationException extends RuntimeException {

  private final String username;

  public AccountRegistrationException(String username, String message) {
    super(String.format("Failed to register Account[%s] : '%s'", username, message));
    this.username = username;
  }

  public AccountRegistrationException(String username, String message, Throwable cause) {
    super(String.format("Failed to register Account[%s] : '%s'", username, message), cause);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
