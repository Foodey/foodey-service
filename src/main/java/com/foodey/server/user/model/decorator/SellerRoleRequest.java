package com.foodey.server.user.model.decorator;

import com.foodey.server.user.enums.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Seller role request model", name = "SellerRoleRequest")
public class SellerRoleRequest extends NewRoleRequest {

  private String identifyImageFront;

  private String identifyImageBack;

  public SellerRoleRequest() {
    super(RoleType.SELLER);
  }

  public SellerRoleRequest(
      String userId,
      String userPhoneNumber,
      String userName,
      String identifyImageFront,
      String identifyImageBack) {
    super(userId, userPhoneNumber, userName, RoleType.SELLER);
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
