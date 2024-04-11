package com.foodey.server.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1L;

  private final HandlerExceptionResolver resolver;

  public AuthEntryPointJwt(
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
      throws IOException {
    log.error("User is unauthorised. Routing from the entry point {}", ex.getMessage());

    if (request.getAttribute("jakarta.servlet.error.exception") != null) {
      Throwable throwable = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
      resolver.resolveException(request, response, null, (Exception) throwable);
    }

    if (!response.isCommitted()) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    }
  }
}
