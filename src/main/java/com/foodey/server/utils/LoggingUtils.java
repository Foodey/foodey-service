package com.foodey.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class LoggingUtils {

  private static ObjectMapper objectMapper = new ObjectMapper();

  @SneakyThrows
  public static void log(Object object) {
    System.out.println(
        object.getClass().getSimpleName()
            + ": "
            + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
  }
}
