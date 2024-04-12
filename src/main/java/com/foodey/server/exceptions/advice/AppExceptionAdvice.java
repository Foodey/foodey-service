package com.foodey.server.exceptions.advice;

import com.foodey.server.common.payload.ExceptionResponse;
import com.foodey.server.exceptions.AccountRegistrationException;
import com.foodey.server.exceptions.InvalidTokenRequestException;
import com.foodey.server.exceptions.NewRoleRequestAlreadySentException;
import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.exceptions.UserLoginException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
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

  @ExceptionHandler(NewRoleRequestAlreadySentException.class)
  @ResponseStatus(HttpStatus.ALREADY_REPORTED)
  @ResponseBody
  public ExceptionResponse handleNewRoleRequestAlreadySentException(
      NewRoleRequestAlreadySentException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, request);
  }

  @ExceptionHandler({InvalidTokenRequestException.class})
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  @ResponseBody
  public ExceptionResponse handleInvalidTokenException(
      InvalidTokenRequestException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, request);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ExceptionResponse handleUserNameNotFoundException(
      UsernameNotFoundException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, HttpStatus.NOT_FOUND, null, request);
  }

  @ExceptionHandler({
    UserLoginException.class,
    BadCredentialsException.class,
    AccountRegistrationException.class,
  })
  @ResponseBody
  @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
  public ExceptionResponse handleUserLoginException(Exception ex, HttpServletRequest request) {
    return new ExceptionResponse(
        ex, HttpStatus.EXPECTATION_FAILED, "Password or username is incorrect", null, request);
  }
}
