package com.foodey.server.security.ratelimmter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

  private final HttpServletRequest request;

  public String username() {
    String name = SecurityContextHolder.getContext().getAuthentication().getName();
    return name.equals("anonymousUser") ? null : name;
  }
}
