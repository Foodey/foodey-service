package com.foodey.server.auth.service;

import com.foodey.server.auth.dto.JwtResponse;
import com.foodey.server.auth.dto.LoginRequest;
import com.foodey.server.auth.dto.LoginResponse;
import com.foodey.server.auth.dto.RegistrationRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

  LoginResponse login(LoginRequest loginRequest);

  JwtResponse refreshToken(HttpServletRequest request) throws ServletException;

  void register(RegistrationRequest request);
}
