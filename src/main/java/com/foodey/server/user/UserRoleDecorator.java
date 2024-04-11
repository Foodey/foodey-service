package com.foodey.server.user;

public abstract class UserRoleDecorator implements UserRole {

  protected UserRole userRole;
  protected NewRoleRequest request;

  public UserRoleDecorator(UserRole userRole, NewRoleRequest request) {
    this.userRole = userRole;
    this.request = request;
  }
}
