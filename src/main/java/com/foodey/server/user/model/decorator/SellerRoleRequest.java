package com.foodey.server.user.model.decorator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodey.server.upload.model.CloudinaryImage;
import com.foodey.server.user.enums.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
@Schema(description = "Seller role request model", name = "SellerRoleRequest")
public class SellerRoleRequest extends NewRoleRequest {

  private CloudinaryImage cldIdentifyImageFront;

  private CloudinaryImage cldIdentifyImageBack;

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
    setIdentifyImageFront(identifyImageFront);
    setIdentifyImageBack(identifyImageBack);
  }

  public String getIdentifyImageFront() {
    return cldIdentifyImageFront != null ? cldIdentifyImageFront.getUrl() : "";
  }

  @JsonProperty("identifyImageFront")
  public void setIdentifyImageFront(String identifyImageFront) {
    if (StringUtils.hasText(identifyImageFront)) {
      this.cldIdentifyImageFront = new CloudinaryImage("user-identify-images");
    }
  }

  public String getIdentifyImageBack() {
    return cldIdentifyImageBack != null ? cldIdentifyImageBack.getUrl() : "";
  }

  @JsonProperty("identifyImageBack")
  public void setIdentifyImageBack(String identifyImageBack) {
    if (StringUtils.hasText(identifyImageBack)) {
      this.cldIdentifyImageBack = new CloudinaryImage("user-identify-images");
    }
  }
}
