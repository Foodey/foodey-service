package com.foodey.server.otp;

import java.util.function.Function;

public interface OTPService {

  void validate(String reciever, String otp, OTPProperties props);

  String send(String reciever, OTPProperties properties);

  String send(String reciever, OTPProperties properties, Function<String, String> messageFunction);

  default String sendAsync(String reciever, OTPProperties properties) {
    return send(reciever, properties);
  }

  default String sendAsync(
      String reciever, OTPProperties properties, Function<String, String> messageFunction) {
    return send(reciever, properties, messageFunction);
  }

  String generateOtp();
}
