package com.foodey.server.user;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindException;

public class SellerRoleDecorator extends UserRoleDecorator {

  public SellerRoleDecorator(UserRole userRole, NewRoleRequest request) {
    super(userRole, request);
  }

  @Override
  public User upgradeRole() throws BindException {
    if (request instanceof SellerRoleRequest) {
      try {
        if (getProfiles() == null) setProfiles(new HashMap<>());
        SellerRoleRequest sellerRequest = (SellerRoleRequest) request;
        getRoles().add(new Role(RoleType.SELLER));

        for (Field field : sellerRequest.getClass().getDeclaredFields()) {
          field.setAccessible(true);
          String name = field.getName();

          Object value = field.get(sellerRequest);

          if (getProfiles().get(name) == null && value != null) {
            getProfiles().put(name, value);
          } else {
            throw new BindException(request, "request");
          }
        }
      } catch (IllegalAccessException e) {
        ReflectionUtils.handleReflectionException(e);
      }
    }
    return userRole.upgradeRole();
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
