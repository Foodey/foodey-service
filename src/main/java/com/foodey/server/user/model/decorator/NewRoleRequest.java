package com.foodey.server.user.model.decorator;

import com.foodey.server.user.enums.RoleType;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "new_role_requests")
@NoArgsConstructor
public class NewRoleRequest {

  @Id private String id;

  private String userId;

  private RoleType role;

  public NewRoleRequest(RoleType role) {
    this.role = role;
  }

  public NewRoleRequest(String userId, RoleType role) {
    this.userId = userId;
    this.role = role;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public RoleType getRole() {
    return role;
  }

  public void setRole(RoleType role) {
    this.role = role;
  }
}
