package com.foodey.server.notify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodey.server.exceptions.SMSNotificationException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service(NotificationType.SMS)
public class SmsNotificationServiceImpl implements NotificationService {

  @Value("${foodey.sms.httpsms.api-key}")
  private String apiKey;

  @Value("${foodey.sms.httpsms.sender}")
  private String sender;

  @Override
  public <R> void sendNotification(R receiver, String message, Object... args) {
    assert receiver instanceof String;

    try {
      URL url = new URL("https://api.httpsms.com/v1/messages/send");
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("POST");
      con.setRequestProperty("accept", "application/json");
      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("x-api-key", apiKey);

      Map<String, String> content = new HashMap<>();
      content.put("content", message);
      content.put("from", sender);
      content.put("to", (String) receiver);

      con.setDoOutput(true);

      try (OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream())) {
        writer.write(new ObjectMapper().writeValueAsString(content));
        writer.flush();
        writer.close();
      }

      InputStream responseStream =
          con.getResponseCode() / 100 == 2 ? con.getInputStream() : con.getErrorStream();
      Scanner scanner = new Scanner(responseStream).useDelimiter("\\A");
      String response = scanner.hasNext() ? scanner.next() : "";
      scanner.close();

      @SuppressWarnings("unchecked")
      final Map<String, Object> responseMap = new ObjectMapper().readValue(response, Map.class);

      if (responseMap.containsKey("failed_at")) {
        String failedReason = (String) responseMap.get("failure_reason");
        log.error("SMS failed: " + failedReason);

        throw new SMSNotificationException(receiver, "SMS failed: " + failedReason);
      }

      log.info("SMS response: " + response);

    } catch (Exception e) {
      log.error("Error SMS " + e);
    }
  }
}
