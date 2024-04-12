package com.foodey.server.auth.service;

import com.foodey.server.auth.model.JwtResponse;
import com.foodey.server.auth.model.LoginRequest;
import com.foodey.server.auth.model.LoginResponse;
import com.foodey.server.auth.model.RegistrationRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

  LoginResponse login(LoginRequest loginRequest);

  JwtResponse refreshToken(HttpServletRequest request) throws ServletException;

  void register(RegistrationRequest request);
}
