package com.foodey.server.auth.controller;

import com.foodey.server.annotation.PublicEndpoint;
import com.foodey.server.auth.fido2.WebAuthnCreationForm;
import com.foodey.server.auth.fido2.WebAuthnService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@PublicEndpoint
@RequestMapping("/api/v1/auth/webauthn")
public class WebAuthnController {

  private final WebAuthnService webAuthnService;

  @PostMapping(value = "/signup")
  public boolean signup(
      HttpServletRequest request, @Valid @RequestBody WebAuthnCreationForm userCreateForm) {
    return webAuthnService.signUp(request, userCreateForm);
  }
}
