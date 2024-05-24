package com.foodey.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

/** UnauthenticatedUserException */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthenticatedUserException extends AuthenticationException {

  public UnauthenticatedUserException() {
    super("User is not authenticated");
  }

  public UnauthenticatedUserException(String msg) {
    super(msg);
  }
}
