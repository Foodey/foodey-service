package com.foodey.server.user;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "new_role_requests")
public class NewRoleRequest {
  private String userId;

  public NewRoleRequest(String userId) {
    this.userId = userId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
