package com.foodey.server.notify;

public interface NotificationService {

  <R> void sendNotification(R receiver, String message);
}
