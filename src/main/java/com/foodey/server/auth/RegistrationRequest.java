package com.foodey.server.auth;

import com.foodey.server.validation.annotation.OptimizedName;
import com.foodey.server.validation.annotation.Password;
import com.foodey.server.validation.annotation.PhoneNumber;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {

  @PhoneNumber private String phoneNumber;

  @Password private String password;

  @OptimizedName private String name;

  // private Set<Role> roles = new HashSet<>();
}
