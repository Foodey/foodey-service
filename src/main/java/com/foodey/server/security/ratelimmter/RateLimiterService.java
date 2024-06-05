package com.foodey.server.security.ratelimmter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateLimiterService {

  public String username() {
    String name = SecurityContextHolder.getContext().getAuthentication().getName();
    return name.equals("anonymousUser") ? null : name;
  }

  public String getRemoteAddress() {
    RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
      log.info("Remote address: {}", request.getRemoteAddr());
      return request.getRemoteAddr();
    }
    return null;
  }

  public String getClientIp(HttpServletRequest request) {
    String ipAddress = request.getHeader("X-Forwarded-For");
    if (ipAddress == null || ipAddress.isEmpty()) {
      ipAddress = request.getRemoteAddr();
    } else {
      // In case of multiple IPs, take the first one
      ipAddress = ipAddress.split(",")[0].trim();
    }
    return ipAddress;
  }
}
