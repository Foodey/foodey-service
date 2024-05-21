package com.foodey.server.notify;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service(NotificationType.EMAIL)
public class EmailNotificationServiceImpl implements NotificationService {

  @Value("${foodey.email.from}")
  private String sender;

  private final JavaMailSender mailSender;

  @Override
  public <R> void sendNotification(R receiver, String message, Object... args) {
    assert receiver instanceof String;
    assert args.length > 0 : "No Subject provided";
    assert args[0] instanceof String : "Subject must be a string";

    SimpleMailMessage mailMessage = new SimpleMailMessage();

    mailMessage.setTo((String) receiver);
    mailMessage.setSubject((String) args[0]);
    mailMessage.setText(message);
    mailMessage.setFrom(sender);

    mailSender.send(mailMessage);
  }
}
