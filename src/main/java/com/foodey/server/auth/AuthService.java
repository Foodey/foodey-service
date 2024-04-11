package com.foodey.server.auth;

import com.foodey.server.user.NewRoleRequest;
import com.foodey.server.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.BindException;

public interface AuthService {

  LoginResponse login(LoginRequest loginRequest);

  JwtResponse refreshToken(HttpServletRequest request) throws ServletException;

  void register(RegistrationRequest request);

  void upgradeRole(NewRoleRequest request, User user) throws BindException;
}
