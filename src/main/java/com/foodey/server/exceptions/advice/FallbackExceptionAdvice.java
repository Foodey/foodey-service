package com.foodey.server.exceptions.advice;

import com.foodey.server.common.payload.ExceptionResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class FallbackExceptionAdvice {

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @RequestBody
  public ExceptionResponse handleIllegalArgumentException(
      IllegalArgumentException e, HttpServletRequest request) {
    return new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @RequestBody
  public ExceptionResponse handleUnwantedException(Exception e, HttpServletRequest request) {
    return new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
  }
}
