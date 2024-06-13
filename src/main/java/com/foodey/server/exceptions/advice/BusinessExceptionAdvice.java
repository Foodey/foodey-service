package com.foodey.server.exceptions.advice;

import com.foodey.server.common.payload.ExceptionResponse;
import com.foodey.server.shop.exceptions.MenuSizeTooBigException;
import com.foodey.server.voucher.VoucherInvalidException;
import jakarta.servlet.http.HttpServletRequest;
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
      MenuSizeTooBigException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, HttpStatus.FORBIDDEN, null, request);
  }

  @ResponseBody
  @ExceptionHandler({VoucherInvalidException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ExceptionResponse handleVoucherInvalidException(
      VoucherInvalidException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, request);
  }
}
