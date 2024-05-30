package com.foodey.server.otp;

import com.foodey.server.notify.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** OTP */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OTPProperties {

  private OTPExpiration otpExpiration;

  @Default private OTPType otpType = OTPType.USER_REGISTRATION;

  @Default private String notificationType = NotificationType.SMS;

  @Default private long ttl = 5 * 60 * 1000;
}
