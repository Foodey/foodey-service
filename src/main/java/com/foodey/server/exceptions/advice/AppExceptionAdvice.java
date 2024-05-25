package com.foodey.server.exceptions.advice;

import com.foodey.server.common.payload.ExceptionResponse;
import com.foodey.server.exceptions.InvalidTokenRequestException;
import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.exceptions.TooManyRequestsException;
import com.foodey.server.exceptions.UserLoginException;
import com.foodey.server.notify.SMSNotificationException;
import com.foodey.server.user.exceptions.NewRoleRequestAlreadySentException;
import com.giffing.bucket4j.spring.boot.starter.context.RateLimitException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppExceptionAdvice {

  @ExceptionHandler({ResourceAlreadyInUseException.class})
  @ResponseStatus(HttpStatus.CONFLICT)
  @ResponseBody
  public ExceptionResponse handleResourceAlreadyInUseException(
      ResourceAlreadyInUseException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, request);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ExceptionResponse handleResourceNotFoundException(
      ResourceNotFoundException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, request);
  }

  @ExceptionHandler(TooManyRequestsException.class)
  @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
  @ResponseBody
  public ExceptionResponse handleTooManyRequestsException(
      TooManyRequestsException e, HttpServletRequest request) {
    return new ExceptionResponse(e, HttpStatus.TOO_MANY_REQUESTS, null, request);
  }

  @ExceptionHandler(NewRoleRequestAlreadySentException.class)
  @ResponseStatus(HttpStatus.ALREADY_REPORTED)
  @ResponseBody
  public ExceptionResponse handleNewRoleRequestAlreadySentException(
      NewRoleRequestAlreadySentException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, request);
  }

  @ExceptionHandler(InvalidTokenRequestException.class)
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  @ResponseBody
  public ExceptionResponse handleInvalidTokenException(
      InvalidTokenRequestException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, request);
  }

  @ExceptionHandler({
    UserLoginException.class,
    UsernameNotFoundException.class,
    BadCredentialsException.class,
  })
  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ExceptionResponse handleUserLoginException(Exception ex, HttpServletRequest request) {
    return new ExceptionResponse(
        ex, HttpStatus.NOT_FOUND, "Password or username is incorrect", null, request);
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ExceptionResponse handleAccessDeniedException(
      AccessDeniedException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, HttpStatus.FORBIDDEN, null, request);
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ExceptionResponse handleAuthenticationException(
      AuthenticationException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, HttpStatus.UNAUTHORIZED, null, request);
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
    return new ExceptionResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, null, request);
  }
}
