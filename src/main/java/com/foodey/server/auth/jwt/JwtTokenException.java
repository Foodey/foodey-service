package com.foodey.server.auth.jwt;

import com.foodey.server.auth.enums.TokenType;
import com.foodey.server.exceptions.BaseException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * Thrown when an invalid token request is made. For example, when the token is expired or the token
 * is malformed.
 */
@Getter
@Setter
public class JwtTokenException extends BaseException {

  private final TokenType tokenType;
  private final String token;

  public JwtTokenException(TokenType tokenType, String token, String message) {
    super(
        HttpStatus.UNAUTHORIZED,
        String.format("%s: [%s] token: [%s] ", message, tokenType.name(), token));
    this.tokenType = tokenType;
    this.token = token;
  }

  public JwtTokenException(String token, String message) {
    super(
        HttpStatus.UNAUTHORIZED,
        String.format("%s: [%s] token: [%s] ", message, TokenType.BEARER, token));
    this.tokenType = TokenType.BEARER;
    this.token = token;
  }
}
