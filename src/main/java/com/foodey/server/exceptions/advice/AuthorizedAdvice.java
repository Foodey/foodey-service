package com.foodey.server.exceptions.advice;

import com.foodey.server.auth.jwt.JwtTokenException;
import com.foodey.server.common.payload.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthorizedAdvice {

  @Operation(
      summary = "Handle User Login Exception",
      description =
          "Handles exceptions related to user login issues such as incorrect username or password.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "404",
            description = "User Not Found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)))
      })
  @ExceptionHandler({
    UsernameNotFoundException.class,
    BadCredentialsException.class,
  })
  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ExceptionResponse handleUserLoginException(Exception ex, HttpServletRequest request) {
    return new ExceptionResponse(
        ex, HttpStatus.NOT_FOUND, "Password or username is incorrect", null, request);
  }

  @Operation(
      summary = "Handle Access Denied Exception",
      description = "Handles exceptions when access is denied to a resource.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)))
      })
  @ExceptionHandler({AccessDeniedException.class, LockedException.class, DisabledException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ExceptionResponse handleAccessDeniedException(Exception ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, HttpStatus.FORBIDDEN, null, request);
  }

  @Operation(
      summary = "Handle Authentication Exception",
      description = "Handles exceptions related to authentication issues.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)))
      })
  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ExceptionResponse handleAuthenticationException(
      AuthenticationException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, HttpStatus.UNAUTHORIZED, null, request);
  }

  @Operation(
      summary = "Handle JwtToken Exception",
      description = "Handles exceptions related to invalid jwt token.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)))
      })
  @ExceptionHandler(JwtTokenException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public ExceptionResponse handleInvalidTokenException(
      JwtTokenException ex, HttpServletRequest request) {
    return new ExceptionResponse(ex, request);
  }
}
