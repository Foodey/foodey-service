package com.foodey.server.common.payload;

/** IPasswordResetRequest */
public interface IPasswordResetRequest extends IBodyRequest {

  String getPassword();

  String getConfirmPassword();
}
