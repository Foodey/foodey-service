package com.foodey.server.auth;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void register(
      @Param(value = "The RegistrationRequest payload") @RequestBody @Valid
          RegistrationRequest signUpRequest) {
    authService.register(signUpRequest);
  }

  @PostMapping("/login")
  @Operation(summary = "Logs the user in to the system and return the auth tokens")
  public ResponseEntity<?> login(
      @Param(value = "The LoginRequest payload") @RequestBody @Valid LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/refresh-token")
  @Operation(summary = "Refresh the expired jwt authentication")
  public ResponseEntity<?> refreshToken(HttpServletRequest request) throws ServletException {
    return ResponseEntity.ok(authService.refreshToken(request));
  }
}
