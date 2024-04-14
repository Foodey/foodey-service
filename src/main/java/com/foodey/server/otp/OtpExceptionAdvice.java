package com.foodey.server.otp;

import com.foodey.server.common.payload.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OtpExceptionAdvice {

  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  @ExceptionHandler(OTPException.class)
  public ExceptionResponse handleOtpException(OTPException e, HttpServletRequest request) {
    return new ExceptionResponse(e, request);
  }
}
