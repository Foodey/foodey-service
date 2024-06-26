package com.foodey.server.exceptions.advice;

import com.foodey.server.common.payload.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

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
  //
  //
  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponse handleValidationException(
      ValidationException e, HttpServletRequest request) {
    return new ExceptionResponse(e, HttpStatus.BAD_REQUEST, null, request);
  }

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

  @Operation(
      summary = "Handle MissingServletRequestPartException",
      description = "Handles MissingServletRequestPartException and returns a structured response")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)))
      })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  @ExceptionHandler(MissingServletRequestPartException.class)
  public ExceptionResponse handleMissingServletRequestPartException(
      MissingServletRequestPartException e, HttpServletRequest request) {
    return new ExceptionResponse(e, HttpStatus.BAD_REQUEST, null, request);
  }

  @Operation(
      summary = "Handle IllegalAccessException",
      description = "Handles IllegalAccessException and returns a structured response")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)))
      })
  @ExceptionHandler(IllegalAccessException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ExceptionResponse handleIllegalArgumentException(
      IllegalAccessException e, HttpServletRequest request) {
    return new ExceptionResponse(e, HttpStatus.BAD_REQUEST, null, request);
  }

  @Operation(
      summary = "Handle BadRequestException",
      description = "Handles BadRequestException and returns a structured response")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)))
      })
  @ExceptionHandler({BadRequestException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ExceptionResponse handleBadRequestException(
      BadRequestException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, HttpStatus.BAD_REQUEST, null, request);
  }
}
