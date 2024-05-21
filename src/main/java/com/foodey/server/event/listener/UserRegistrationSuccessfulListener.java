package com.foodey.server.event.listener;

import com.foodey.server.event.UserRegistrationSuccessfulEvent;
import com.foodey.server.notify.NotificationFactory;
import com.foodey.server.notify.NotificationType;
import com.foodey.server.otp.OTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationSuccessfulListener {

  private final NotificationFactory notificationFactory;
  private final OTPService otpService;

  @Async
  @TransactionalEventListener
  public void onEventTrigger(UserRegistrationSuccessfulEvent event) {
    log.info("User {} registration event handled successfully", event.getUser().getUsername());

    otpService.generate(event.getUser().getUsername());

    notificationFactory.execute(
        NotificationType.SMS,
        event.getUser().getPhoneNumber(),
        "Welcome to Foodey! Your account has been successfully created. Enjoy your time with us!");
  }
}
