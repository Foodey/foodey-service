package com.foodey.server.auth.event.listener;

import com.foodey.server.auth.event.UserRegistrationSuccessfulEvent;
import com.foodey.server.auth.event.UserWaitingOTPValidationEvent;
import com.foodey.server.exceptions.DatabaseErrorException;
import com.foodey.server.notify.NotificationType;
import com.foodey.server.otp.OTPExpiration;
import com.foodey.server.otp.OTPService;
import com.foodey.server.otp.OTPType;
import com.foodey.server.otp.OTPValidatedEvent;
import com.foodey.server.user.enums.UserStatus;
import com.foodey.server.user.model.User;
import com.foodey.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthEventListener {

  private final OTPService otpService;

  private final UserService userService;
  private final ApplicationEventPublisher eventPublisher;

  @Async
  @TransactionalEventListener
  public void onUserWaitingOtpValidationEvent(UserWaitingOTPValidationEvent event) {
    log.info("User {} waiting for OTP validation", event.getUser().getPhoneNumber());
    otpService.send(NotificationType.SMS, event.getUser().getPhoneNumber(), OTPExpiration.SHORT);
  }

  @TransactionalEventListener
  public void onOTPValidatedEvent(OTPValidatedEvent event) throws DatabaseErrorException {
    if (event.getType() == OTPType.USER_REGISTRATION) {
      User user =
          userService
              .findByPhoneNumber(event.getId())
              .orElseThrow(
                  () -> {
                    log.error("User with phone number {} not found", event.getId());
                    return new DatabaseErrorException(
                        "Missing user with phone number " + event.getId());
                  });

      user.setStatus(UserStatus.ACTIVE);
      userService.save(user);
      eventPublisher.publishEvent(new UserRegistrationSuccessfulEvent(this, user));
    }
  }
}
