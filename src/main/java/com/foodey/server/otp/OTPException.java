package com.foodey.server.otp;

import com.foodey.server.exceptions.HttpException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class OTPException extends HttpException {

  private String otp;

  public OTPException(String message, String otp) {
    super(HttpStatus.NOT_ACCEPTABLE, String.format("%s with otp: %s", message, otp));
  }
}
