package com.foodey.server.auth.controller;

import com.foodey.server.annotation.PublicEndpoint;
import com.foodey.server.auth.dto.LoginRequest;
import com.foodey.server.auth.dto.RegistrationRequest;
import com.foodey.server.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@PublicEndpoint
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "Registers a new user to the system")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "User registered successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "409", description = "User already exists"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void register(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The RegistrationRequest payload")
          @RequestBody
          @Valid
          RegistrationRequest signUpRequest) {
    authService.register(signUpRequest);
  }

  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "User logged in successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "404", description = "User not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping("/login")
  @Operation(summary = "Logs the user in to the system and return the auth tokens")
  public ResponseEntity<?> login(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The LoginRequest payload")
          @RequestBody
          @Valid
          LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/refresh-token")
  @Operation(summary = "Refresh the expired jwt authentication")
  public ResponseEntity<?> refreshToken(HttpServletRequest request) throws ServletException {
    return ResponseEntity.ok(authService.refreshToken(request));
  }
}
