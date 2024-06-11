package com.foodey.server.user.model.decorator;

import com.foodey.server.user.enums.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "new_role_requests")
@NoArgsConstructor
@Getter
public class NewRoleRequest {

  @Id private String id;

  private String userId;

  @Setter private String userPhoneNumber;

  @Setter private String userName;

  private RoleType role;

  public NewRoleRequest(RoleType role) {
    this.role = role;
  }

  public NewRoleRequest(String userId, String userPhoneNumber, String userName, RoleType role) {
    this.userId = userId;
    this.role = role;
    this.userPhoneNumber = userPhoneNumber;
    this.userName = userName;
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
