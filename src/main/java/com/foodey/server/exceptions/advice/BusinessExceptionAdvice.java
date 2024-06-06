package com.foodey.server.exceptions.advice;

import com.foodey.server.common.payload.ExceptionResponse;
import com.foodey.server.shop.exceptions.MenuSizeTooBigException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BusinessExceptionAdvice {

  @ExceptionHandler({MenuSizeTooBigException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public ExceptionResponse handleMenuSizeTooBigException(
      BadRequestException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, HttpStatus.FORBIDDEN, null, request);
  }
}
