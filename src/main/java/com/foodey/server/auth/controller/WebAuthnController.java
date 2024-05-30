package com.foodey.server.auth.controller;

import com.foodey.server.annotation.PublicEndpoint;
import com.foodey.server.auth.dto.JwtResponse;
import com.foodey.server.auth.fido2.WebAuthnRegistrationRequest;
import com.foodey.server.auth.fido2.WebAuthnService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PublicEndpoint
@RequestMapping("/api/v1/auth/webauthn")
public class WebAuthnController {

  private final WebAuthnService webAuthnService;

  @PostMapping(value = "/register")
  public JwtResponse signup(
      HttpServletRequest request,
      @Valid @RequestBody WebAuthnRegistrationRequest webAuthnRegistrationRequest) {
    return webAuthnService.register(request, webAuthnRegistrationRequest);
  }
}
