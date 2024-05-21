package com.foodey.server.otp;

public interface OTPService {

  void validate(String id, String otp, OTPType otpType);

  String send(String notificationType, String id, OTPExpiration expiration);

  String send(String notificationType, String id, long expiredAfterMs);

  String generateOtp();
}
