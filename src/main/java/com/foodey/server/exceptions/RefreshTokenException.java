package com.foodey.server.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class RefreshTokenException extends RuntimeException {

  private final String token;
  private final String reason;

  public RefreshTokenException(String token, String reason) {
    super(String.format("Couldn't refresh token for [%s] since [%s] )", token, reason));
    this.token = token;
    this.reason = reason;
  }
}
