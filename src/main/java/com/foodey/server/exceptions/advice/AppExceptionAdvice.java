package com.foodey.server.exceptions.advice;

import com.foodey.server.common.payload.ExceptionResponse;
import com.foodey.server.exceptions.TooManyRequestsException;
import com.foodey.server.notify.httpsms.SMSNotificationException;
import com.foodey.server.user.exceptions.NewRoleRequestAlreadySentException;
import com.giffing.bucket4j.spring.boot.starter.context.RateLimitException;
import com.webauthn4j.util.exception.WebAuthnException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class AppExceptionAdvice {

  @ExceptionHandler(NewRoleRequestAlreadySentException.class)
  @ResponseStatus(HttpStatus.ALREADY_REPORTED)
  @ResponseBody
  public ExceptionResponse handleNewRoleRequestAlreadySentException(
      NewRoleRequestAlreadySentException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, request);
  }

  @ExceptionHandler(WebAuthnException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ExceptionResponse handleWebAuthnException(
      WebAuthnException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, HttpStatus.FORBIDDEN, null, request);
  }

  @ExceptionHandler(TooManyRequestsException.class)
  @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
  @ResponseBody
  public ExceptionResponse handleTooManyRequestsException(
      TooManyRequestsException e, HttpServletRequest request) {
    return new ExceptionResponse(e, request);
  }

  @ExceptionHandler(value = {RateLimitException.class})
  @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
  public ExceptionResponse handleRateLimit(RateLimitException e, HttpServletRequest request) {
    return new ExceptionResponse(e, HttpStatus.TOO_MANY_REQUESTS, null, request);
  }

  @ExceptionHandler(SMSNotificationException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ExceptionResponse handleSMSNotificationException(
      SMSNotificationException ex, HttpServletRequest request) {
    log.error("SMS Notification Exception: ", ex);
    return new ExceptionResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, null, request);
  }
}
