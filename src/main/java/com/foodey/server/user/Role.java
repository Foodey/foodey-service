package com.foodey.server.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(name = "Role", description = "Role of the user")
public class Role {

  @Schema(description = "Role name", required = true)
  private RoleType name;

  @Schema(description = "Role permissions")
  private Set<Permission> permissions;

  public Role() {}

  public Role(RoleType name) {
    this.name = name;
    this.permissions = null;
  }

  public Role(RoleType name, Set<Permission> permissions) {
    this.name = name;
    this.permissions = permissions;
  }

  public void addPermission(Permission permission) {
    permissions.add(permission);
  }

  public void removePermission(Permission permission) {
    permissions.remove(permission);
  }

  public boolean hasPermission(Permission permission) {
    return permissions.contains(permission);
  }

  public boolean isSamePermission(Role role) {
    return permissions.containsAll(role.permissions);
  }

  public RoleType getName() {
    return name;
  }

  public void setName(RoleType name) {
    this.name = name;
  }

  public Set<Permission> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<Permission> permissions) {
    this.permissions = permissions;
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    else if (!(obj instanceof Role)) return false;
    return name.equals(((Role) obj).name);
  }
}
