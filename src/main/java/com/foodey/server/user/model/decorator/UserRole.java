package com.foodey.server.user.model.decorator;

import com.foodey.server.user.model.Role;
import com.foodey.server.user.model.User;
import java.util.Map;
import java.util.Set;

public interface UserRole {

  /// Get the user's profile information as a map.
  /// When new roles are added, we will have some extra information that we want to store
  /// and we don't know what that will be yet. So we will return a map of the profile
  public Map<String, Object> getProfiles();

  public void setProfiles(Map<String, Object> profiles);

  public Set<Role> getRoles();

  public void setRoles(Set<Role> roles);

  public User registerRole();
}
