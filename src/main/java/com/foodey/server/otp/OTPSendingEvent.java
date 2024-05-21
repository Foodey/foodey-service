package com.foodey.server.otp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OTPSendingEvent extends ApplicationEvent {

  private String receiver;
  private String otp;
  private String message;
  private OTPProperties otpProperties;

  public OTPSendingEvent(
      Object source, String receiver, String otp, String message, OTPProperties props) {
    super(source);
    this.receiver = receiver;
    this.otp = otp;
    this.otpProperties = props;
    this.message = message;
  }
}
