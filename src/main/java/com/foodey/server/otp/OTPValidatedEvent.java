package com.foodey.server.otp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OTPValidatedEvent extends ApplicationEvent {

  private String otp;
  private String receiver;
  private OTPProperties otpProperties;

  public OTPValidatedEvent(Object source, String receiver, String otp, OTPProperties properties) {
    super(source);
    this.receiver = receiver;
    this.otp = otp;
    this.otpProperties = properties;
  }
}
