package com.foodey.server.user.model.decorator;

import java.lang.reflect.Field;
import java.util.HashMap;

public abstract class UserRoleDecorator implements UserRole {

  protected UserRole userRole;
  protected NewRoleRequest request;

  public UserRoleDecorator(UserRole userRole, NewRoleRequest request) {
    this.userRole = userRole;
    this.request = request;
  }

  protected void addProfiles() {
    if (getProfiles() == null) setProfiles(new HashMap<>());

    try {
      for (Field field : request.getClass().getDeclaredFields()) {
        field.setAccessible(true);
        Object value = field.get(request);
        if (value != null) getProfiles().putIfAbsent(field.getName(), value);
      }
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}
