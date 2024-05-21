package com.foodey.server.auth.service.impl;

import com.foodey.server.auth.dto.JwtResponse;
import com.foodey.server.auth.dto.LoginRequest;
import com.foodey.server.auth.dto.LoginResponse;
import com.foodey.server.auth.dto.RegistrationRequest;
import com.foodey.server.auth.enums.TokenType;
import com.foodey.server.auth.event.UserWaitingOTPValidationEvent;
import com.foodey.server.auth.model.RefreshToken;
import com.foodey.server.auth.repository.RefreshTokenRepository;
import com.foodey.server.auth.service.AuthService;
import com.foodey.server.exceptions.InvalidTokenRequestException;
import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.UserLoginException;
import com.foodey.server.notify.NotificationType;
import com.foodey.server.otp.OTPExpiration;
import com.foodey.server.otp.OTPProperties;
import com.foodey.server.otp.OTPService;
import com.foodey.server.security.jwt.JwtService;
import com.foodey.server.user.model.User;
import com.foodey.server.user.service.UserService;
import com.foodey.server.utils.HttpHeaderUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final RefreshTokenRepository refreshTokenRepository;

  private final AuthenticationManager authenticationManager;
  private final ApplicationEventPublisher applicationEventPulisher;

  private final UserService userService;
  private final JwtService jwtService;
  private final OTPService otpService;

  @Override
  public LoginResponse login(LoginRequest loginRequest) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getPhoneNumber(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    User user = (User) authentication.getPrincipal();

    if (user == null) throw new UserLoginException("Account not found");

    final String accessToken = jwtService.generateAccessToken(user);
    final RefreshToken refreshToken = jwtService.generateRefreshToken(user);

    refreshTokenRepository.save(refreshToken);

    log.info("Account {} logged in successfully", user.getUsername());

    return LoginResponse.builder()
        .name(user.getName())
        .jwt(new JwtResponse(accessToken, refreshToken.getToken()))
        .build();
  }

  @Override
  public JwtResponse refreshToken(HttpServletRequest request) throws ServletException {
    final String refreshToken = HttpHeaderUtils.extractBearerToken(request);

    RefreshToken savedRefreshToken =
        refreshTokenRepository
            .findByToken(refreshToken)
            .orElseThrow(
                () -> {
                  log.error("Refresh token {} not found", refreshToken);
                  return new InvalidTokenRequestException(
                      TokenType.BEARER, refreshToken, "Invalid refresh token");
                });

    if (savedRefreshToken.isRevoked()) {
      throw new InvalidTokenRequestException(
          TokenType.BEARER, refreshToken, "Refresh token revoked");
    } else if (savedRefreshToken.isExpired()) {
      refreshTokenRepository.save(savedRefreshToken.revoke());
      throw new InvalidTokenRequestException(
          TokenType.BEARER, refreshToken, "Refresh token expired");
    }

    String newAccessToken = jwtService.generateAccessToken(savedRefreshToken.getUserPubId());
    RefreshToken updatedRefreshToken = refreshTokenRepository.save(savedRefreshToken.refresh());

    log.info(
        "Refresh token {} refreshed successfully for user with public id {}",
        refreshToken,
        savedRefreshToken.getUserPubId());

    return new JwtResponse(newAccessToken, updatedRefreshToken.getToken());
  }

  @Override
  @Transactional
  public void register(RegistrationRequest request) {
    final String phoneNumber = request.getPhoneNumber();

    if (userService.existsByPhoneNumber(phoneNumber))
      throw new ResourceAlreadyInUseException("User", "phoneNumber", phoneNumber);

    User user = userService.save(userService.createBasicUser(request));
    OTPProperties otpProperties =
        OTPProperties.builder()
            .notificationType(NotificationType.SMS)
            .otpExpiration(OTPExpiration.SHORT)
            .build();

    otpService.send(
        user.getPhoneNumber(),
        otpProperties,
        (otp) ->
            "Welcome to Foodey! Your OTP is "
                + otp
                + ". Please enter this OTP to complete your registration.");

    applicationEventPulisher.publishEvent(new UserWaitingOTPValidationEvent(this, user));
  }
}
