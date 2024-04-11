package com.foodey.server.exceptions.advice;

import com.foodey.server.common.payload.ExceptionResponse;
import com.foodey.server.exceptions.AccountRegistrationException;
import com.foodey.server.exceptions.RefreshTokenException;
import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.exceptions.UserLoginException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
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
    return new ExceptionResponse(
        HttpStatus.CONFLICT, ex.getMessage(), null, request, ex.getClass().getName());
  }

  @ExceptionHandler({
    ResourceNotFoundException.class,
  })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ExceptionResponse handleResourceNotFoundException(
      ResourceNotFoundException ex, HttpServletRequest request) {
    return new ExceptionResponse(
        HttpStatus.NOT_FOUND, ex.getMessage(), null, request, ex.getClass().getName());
  }

  @ExceptionHandler({BadRequestException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ExceptionResponse handleBadRequestException(
      BadRequestException ex, HttpServletRequest request) {
    return new ExceptionResponse(
        HttpStatus.BAD_REQUEST, ex.getMessage(), null, request, ex.getClass().getName());
  }

  @ExceptionHandler({
    UsernameNotFoundException.class,
  })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ExceptionResponse handleResourceNotFoundException(
      Exception ex, HttpServletRequest request) {
    return new ExceptionResponse(
        HttpStatus.NOT_FOUND, ex.getMessage(), null, request, ex.getClass().getName());
  }

  @ExceptionHandler({
    UserLoginException.class,
    BadCredentialsException.class,
    AccountRegistrationException.class,
    RefreshTokenException.class,
  })
  @ResponseBody
  @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
  public ExceptionResponse handleUserLoginException(Exception ex, HttpServletRequest request) {
    return new ExceptionResponse(
        HttpStatus.EXPECTATION_FAILED,
        "Password or username is incorrect",
        null,
        request,
        ex.getClass().getName());
  }
}
