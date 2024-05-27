package com.foodey.server.auth.fido2;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface WebAuthnService {

  boolean signUp(
      HttpServletRequest request, WebAuthnRegistrationRequest webAuthnRegistrationRequest);
}
