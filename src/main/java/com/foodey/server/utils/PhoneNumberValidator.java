package com.foodey.server.utils;

import com.foodey.server.common.regexp.PhoneNumberRegexp;
import java.util.Arrays;

public class PhoneNumberValidator {

  public static boolean validate(String phoneNumber) {
    return Arrays.stream(PhoneNumberRegexp.values())
        .anyMatch(regexp -> phoneNumber.matches(regexp.getValue()));
  }
}
