package com.foodey.server.exceptions;

import com.foodey.server.auth.TokenType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidTokenRequestException extends RuntimeException {

  private final TokenType tokenType;
  private final String token;

  public InvalidTokenRequestException(TokenType tokenType, String token, String message) {
    super(String.format("%s: [%s] token: [%s] ", message, tokenType.name(), token));
    this.tokenType = tokenType;
    this.token = token;
  }
}
