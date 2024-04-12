package com.foodey.server.exceptions.advice;

import com.foodey.server.common.payload.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class ValidationExceptionAdvice {

  // @ExceptionHandler(MethodArgumentNotValidException.class)
  // @ResponseStatus(HttpStatus.BAD_REQUEST)
  // public ExceptionResponse handleHandlerMethodValidationException(
  //     MethodArgumentNotValidException e, HttpServletRequest request) {

  //   final Map<String, String> body = new HashMap<>();
  //   e.getFieldErrors
  //       .forEach(
  //           objectError -> {
  //             log.info("Object error: {}", objectError);
  //             final String[] fieldError = objectError.getCodes()[0].split("\\.");
  //             body.put(fieldError[fieldError.length - 1], objectError.getDefaultMessage());
  //           });
  //   return new ExceptionResponse(e, HttpStatus.BAD_REQUEST, body, request);
  // }

  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponse handleBindException(BindException e, HttpServletRequest request) {

    final Map<String, String> body = new HashMap<>();
    e.getFieldErrors()
        .forEach(fieldError -> body.put(fieldError.getField(), fieldError.getDefaultMessage()));

    return new ExceptionResponse(e, HttpStatus.BAD_REQUEST, body, request);
  }

  @ExceptionHandler(HandlerMethodValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponse handleHandlerMethodValidationException(
      HandlerMethodValidationException e, HttpServletRequest request) {

    final Map<String, String> body = new HashMap<>();
    e.getAllErrors()
        .forEach(
            objectError -> {
              final String[] fieldError = objectError.getCodes()[0].split("\\.");
              body.put(fieldError[fieldError.length - 1], objectError.getDefaultMessage());
            });

    return new ExceptionResponse(e, HttpStatus.BAD_REQUEST, body, request);
  }
}
