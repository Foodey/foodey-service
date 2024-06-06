package com.foodey.server.otp;

import com.foodey.server.exceptions.BaseException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class OTPException extends BaseException {

  private String otp;

  public OTPException(String message, String otp) {
    super(HttpStatus.NOT_ACCEPTABLE, String.format("%s with otp: %s", message, otp));
  }
}
