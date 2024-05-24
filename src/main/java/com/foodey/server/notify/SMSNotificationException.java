package com.foodey.server.notify;

import lombok.Getter;

/** SMSNotificationException */
@Getter
public class SMSNotificationException extends RuntimeException {

  private Object receiver;

  public SMSNotificationException(Object receiver, String message) {
    super(message);
    this.receiver = receiver;
  }
}
