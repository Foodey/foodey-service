package com.foodey.server.user.model.decorator;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** SellerRoleRequestRespons */
@Getter
@Setter
@AllArgsConstructor
public class SellerRoleImageUploadOptions implements NewRoleRequestResponse {

  private Map<String, Object> identifyImageFrontOptions;

  private Map<String, Object> identifyImageBackOptions;
}
