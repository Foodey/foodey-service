package com.foodey.server.utils;

import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.user.model.User;
import java.security.Principal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class PrincipalUtils {

  public static final User getUser(Principal connectedUser) {
    if (connectedUser == null) {
      throw new ResourceNotFoundException("User", "", "");
    }

    final var token = (UsernamePasswordAuthenticationToken) connectedUser;
    return (User) token.getPrincipal();
  }
}
