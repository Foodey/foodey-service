package com.foodey.server.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/.well-known")
public class WellKnowController {

  @GetMapping("/assetlinks.json")
  @SneakyThrows
  public Object assetlinks() {
    String json = Files.readString(Paths.get("src/main/resources/.well-known/assetlinks.json"));
    return new ObjectMapper().readValue(json, Object.class);
  }
}
