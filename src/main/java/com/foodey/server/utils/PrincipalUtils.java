package com.foodey.server.utils;

import com.foodey.server.exceptions.UnauthenticatedUserException;
import com.foodey.server.user.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class PrincipalUtils {

  public static final User getUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof User) return (User) principal;
    throw new UnauthenticatedUserException();
  }

  public static final String getUserId() {
    return getUser().getId();
  }

  public static final Object getPrincipal() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public static final String getUsername() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
