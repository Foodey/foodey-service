package com.foodey.server.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Seller role request model", name = "SellerRoleRequest")
public class SellerRoleRequest extends NewRoleRequest {

  private String identifyImageFront;

  private String identifyImageBack;

  public SellerRoleRequest(String userId, String identifyImageFront, String identifyImageBack) {
    super(userId);
    this.identifyImageFront = identifyImageFront;
    this.identifyImageBack = identifyImageBack;
  }

  public String getIdentifyImageFront() {
    return identifyImageFront;
  }

  public void setIdentifyImageFront(String identifyImageFront) {
    this.identifyImageFront = identifyImageFront;
  }

  public String getIdentifyImageBack() {
    return identifyImageBack;
  }

  public void setIdentifyImageBack(String identifyImageBack) {
    this.identifyImageBack = identifyImageBack;
  }
}
