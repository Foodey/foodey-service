package com.foodey.server.otp;

import java.util.function.Function;

public interface OTPService {

  void validate(String reciever, String otp, OTPProperties props);

  String send(String reciever, OTPProperties properties);

  String send(String reciever, OTPProperties properties, Function<String, String> messageFunction);

  String generateOtp();
}
