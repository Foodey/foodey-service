package com.foodey.server.auth.fido2;

import com.foodey.server.auth.dto.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface WebAuthnService {

  LoginResponse register(
      HttpServletRequest request, WebAuthnRegistrationRequest webAuthnRegistrationRequest);

  LoginResponse login(HttpServletRequest request, WebAuthnLoginRequest webAuthnLoginRequest);
}
