package com.foodey.server.auth.event.listener;

import com.foodey.server.auth.event.UserRegistrationSuccessfulEvent;
import com.foodey.server.exceptions.DatabaseErrorException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.otp.OTPType;
import com.foodey.server.otp.OTPValidatedEvent;
import com.foodey.server.user.enums.UserStatus;
import com.foodey.server.user.model.User;
import com.foodey.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthEventListener {

  private final UserService userService;
  private final ApplicationEventPublisher eventPublisher;

  @TransactionalEventListener
  public void onOTPValidatedEvent(OTPValidatedEvent event) throws DatabaseErrorException {
    if (event.getOtpProperties().getOtpType().equals(OTPType.USER_REGISTRATION)) {
      User user =
          userService
              .findByPhoneNumber(event.getReceiver())
              .orElseThrow(
                  () -> {
                    log.error("User with phone number {} not found", event.getReceiver());
                    return new ResourceNotFoundException(
                        "User", "phone number", event.getReceiver());
                  });

      user.setStatus(UserStatus.ACTIVE);
      userService.save(user);
      eventPublisher.publishEvent(new UserRegistrationSuccessfulEvent(this, user));
    }
  }
}
