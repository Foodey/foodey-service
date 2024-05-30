package com.foodey.server.auth.event.listener;

import com.foodey.server.auth.event.UserRegistrationSuccessfulEvent;
import com.foodey.server.exceptions.DatabaseErrorException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.notify.mail.EmailNotificationServiceImpl;
import com.foodey.server.notify.mail.EmailRequest;
import com.foodey.server.notify.mail.EmailType;
import com.foodey.server.otp.OTPType;
import com.foodey.server.otp.OTPValidatedEvent;
import com.foodey.server.user.enums.UserStatus;
import com.foodey.server.user.model.User;
import com.foodey.server.user.service.UserService;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthEventListener {

  private final UserService userService;
  private final ApplicationEventPublisher eventPublisher;
  private final EmailNotificationServiceImpl emailNotificationService;

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

  @TransactionalEventListener
  @Async
  @SneakyThrows
  public void onUserRegistrationSuccessfulEvent(UserRegistrationSuccessfulEvent event) {
    String userEmail = event.getUser().getEmail();

    if (StringUtils.hasText(userEmail)) {

      String htmlContent =
          Files.readString(
              Paths.get("src/main/resources/templates/email/html/greeting/greeting.html"));
      htmlContent.replace("{USER_NAME}", event.getUser().getName());

      EmailRequest emailRequest =
          new EmailRequest(userEmail, htmlContent, "Registration Successful", EmailType.HTML);
      emailNotificationService.sendNotification(emailRequest);
    }
  }
}
