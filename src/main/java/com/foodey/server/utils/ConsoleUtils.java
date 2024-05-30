package com.foodey.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class ConsoleUtils {

  private static ObjectMapper objectMapper = new ObjectMapper();

  @SneakyThrows
  public static void prettyPrint(Object object) {
    if (object == null) {
      System.out.println("No object to log: null");
      return;
    }

    System.out.println(
        object.getClass().getSimpleName()
            + ": "
            + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
  }
}
