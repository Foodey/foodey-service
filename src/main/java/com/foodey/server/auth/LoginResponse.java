package com.foodey.server.auth;

/** LoginRequest */
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
  private String name;

  private String phoneNumber;

  private JwtResponse jwt;
}
