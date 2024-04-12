package com.foodey.server.user.model.decorator;

import com.foodey.server.user.enums.RoleType;
import com.foodey.server.user.model.Role;
import com.foodey.server.user.model.User;
import java.util.Map;
import java.util.Set;

public class SellerRoleDecorator extends UserRoleDecorator {

  public SellerRoleDecorator(UserRole userRole, NewRoleRequest request) {
    super(userRole, request);
  }

  @Override
  public User registerRole() {
    getRoles().add(new Role(RoleType.SELLER));
    addProfiles();
    return userRole.registerRole();
  }

  @Override
  public Map<String, Object> getProfiles() {
    return userRole.getProfiles();
  }

  @Override
  public void setProfiles(Map<String, Object> profiles) {
    userRole.setProfiles(profiles);
  }

  @Override
  public Set<Role> getRoles() {
    return userRole.getRoles();
  }

  @Override
  public void setRoles(Set<Role> roles) {
    userRole.setRoles(roles);
  }
}
