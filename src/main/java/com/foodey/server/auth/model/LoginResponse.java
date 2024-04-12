package com.foodey.server.auth.model;

/** LoginRequest */
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
  private String name;

  private JwtResponse jwt;
}
