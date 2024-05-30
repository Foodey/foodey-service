package com.foodey.server.auth.fido2;

import com.foodey.server.auth.dto.JwtResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface WebAuthnService {

  JwtResponse register(
      HttpServletRequest request, WebAuthnRegistrationRequest webAuthnRegistrationRequest);
}
