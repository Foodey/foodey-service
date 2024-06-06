package com.foodey.server.exceptions.advice;

import com.foodey.server.common.payload.ExceptionResponse;
import com.foodey.server.exceptions.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class FallbackExceptionAdvice {

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<?> handleUnwantedHttpException(
      BaseException e, HttpServletRequest request) {
    return new ExceptionResponse(e, request).toResponseEntity();
  }

  @ExceptionHandler(HttpClientErrorException.class)
  public ResponseEntity<?> handleHttpClientErrorException(
      HttpClientErrorException e, HttpServletRequest request) {
    return new ExceptionResponse(
            e,
            HttpStatus.valueOf(e.getStatusCode().value()),
            e.getStatusText(),
            e.getMessage(),
            request)
        .toResponseEntity();
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
  @ExceptionHandler(UnsupportedOperationException.class)
  public ExceptionResponse handleUnsuuportOperationException(
      UnsupportedOperationException e, HttpServletRequest request) {
    return new ExceptionResponse(e, HttpStatus.EXPECTATION_FAILED, null, request);
  }

  @ResponseBody
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ExceptionResponse handleUnwantedException(Exception e, HttpServletRequest request) {
    log.error(e.getClass().getSimpleName() + ": " + e.getMessage());
    return new ExceptionResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, null, request);
  }
}
