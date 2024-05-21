package com.foodey.server.otp;

import com.foodey.server.notify.NotificationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class OTPEventListener {

  private final NotificationFactory notificationFactory;

  @Async
  @TransactionalEventListener
  public void onOTPSendingEvent(OTPSendingEvent event) {

    log.info("Sending OTP to {}", event.getReceiver());
    notificationFactory.execute(
        event.getOtpProperties().getNotificationType(), event.getReceiver(), event.getMessage());
  }
}
