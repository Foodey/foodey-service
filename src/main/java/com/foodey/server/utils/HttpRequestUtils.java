package com.foodey.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import org.springframework.scheduling.annotation.Async;

public class HttpRequestUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Async
  public static Object post(String url, Object body, Map<String, String> properties) {

    try {
      HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
      con.setRequestMethod("POST");
      properties.forEach(con::setRequestProperty);

      // send request
      con.setDoOutput(true);
      try (OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream())) {
        writer.write(objectMapper.writeValueAsString(body));
        writer.flush();
        writer.close();
      }

      boolean success = con.getResponseCode() / 100 == 2;

      InputStream responseStream = success ? con.getInputStream() : con.getErrorStream();
      String response = "";

      try (Scanner scanner = new Scanner(responseStream).useDelimiter("\\A")) {
        response = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
      }

      if (!success) {
        throw new RuntimeException(
            "Failed : HTTP error code : " + con.getResponseCode() + " response: " + response);
      }

      return objectMapper.readValue(response, Object.class);

    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
