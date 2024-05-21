package com.foodey.server.security.ratelimmter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

  public String username() {
    String name = SecurityContextHolder.getContext().getAuthentication().getName();
    return name.equals("anonymousUser") ? null : name;
  }
}
