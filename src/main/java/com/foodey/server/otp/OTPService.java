package com.foodey.server.otp;

/** OTPService */
public interface OTPService {

  OTP generate(String id);

  OTP generate(String id, long expiredAfterMs);

  OTP generate(String id, boolean fastExpire);

  void validate(String id, String otp);

  void send(String id);

  void send(String id, long expiredAfterMs);

  void send(String id, boolean fastExpire);
}
