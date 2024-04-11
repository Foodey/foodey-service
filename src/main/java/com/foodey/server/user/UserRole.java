package com.foodey.server.user;

import java.util.Map;
import java.util.Set;
import org.springframework.validation.BindException;

public interface UserRole {

  /// Get the user's profile information as a map.
  /// When new roles are added, we will have some extra information that we want to store
  /// and we don't know what that will be yet. So we will return a map of the profile
  public Map<String, Object> getProfiles();

  public void setProfiles(Map<String, Object> profiles);

  public Set<Role> getRoles();

  public void setRoles(Set<Role> roles);

  public User upgradeRole() throws BindException;
}
