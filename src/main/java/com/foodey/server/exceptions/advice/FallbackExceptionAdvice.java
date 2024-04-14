package com.foodey.server.exceptions.advice;

import com.foodey.server.common.payload.ExceptionResponse;
import com.foodey.server.exceptions.HttpException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
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
public class FallbackExceptionAdvice {

  @ExceptionHandler({BadRequestException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ExceptionResponse handleBadRequestException(
      BadRequestException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, HttpStatus.BAD_REQUEST, null, request);
  }

  @ExceptionHandler(IllegalAccessException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ExceptionResponse handleIllegalArgumentException(
      IllegalAccessException e, HttpServletRequest request) {
    return new ExceptionResponse(e, HttpStatus.BAD_REQUEST, null, request);
  }

  @ExceptionHandler(HttpException.class)
  public ResponseEntity<?> handleUnwantedHttpException(
      HttpException e, HttpServletRequest request) {
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

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ExceptionResponse handleUnwantedException(Exception e, HttpServletRequest request) {
    return new ExceptionResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, null, request);
  }
}
