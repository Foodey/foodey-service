package com.foodey.server.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.foodey.server.validation.annotation.OptimizedName;
import com.foodey.server.validation.annotation.Password;
import com.foodey.server.validation.annotation.PhoneNumber;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {

  @PhoneNumber
  @JsonAlias("username")
  private String phoneNumber;

  @Password private String password;

  @OptimizedName private String name;

  @Email private String email;

  public RegistrationRequest() {}

  public RegistrationRequest(String phoneNumber, String password, String name) {
    this.phoneNumber = phoneNumber;
    this.password = password;
    this.name = name;
  }

  public RegistrationRequest(String phoneNumber, String password, String name, String email) {
    this.phoneNumber = phoneNumber;
    this.password = password;
    this.name = name;
    this.email = email;
  }
}
