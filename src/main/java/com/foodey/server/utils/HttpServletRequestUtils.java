package com.foodey.server.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** HttpServletRequestUtils */
public class HttpServletRequestUtils {

  public static HttpServletRequest getRequest() {
    RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
    return attributes != null ? ((ServletRequestAttributes) attributes).getRequest() : null;
  }
}
