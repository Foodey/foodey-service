package com.foodey.server.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

  private String accessToken;

  private String refreshToken;

  private TokenType tokenType;

  public JwtResponse(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.tokenType = TokenType.BEARER;
  }
}
