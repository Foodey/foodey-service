package com.foodey.server.otp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OTPValidatedEvent extends ApplicationEvent {

  private String id;
  private String otp;
  private OTPType type;

  public OTPValidatedEvent(Object source, String id, String otp, OTPType type) {
    super(source);
    this.id = id;
    this.otp = otp;
    this.type = type;
  }
}
