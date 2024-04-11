package com.foodey.server.auth;

import com.foodey.server.exceptions.RefreshTokenException;
import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.UserLoginException;
import com.foodey.server.security.JwtService;
import com.foodey.server.user.NewRoleRequest;
import com.foodey.server.user.SellerRoleDecorator;
import com.foodey.server.user.User;
import com.foodey.server.user.UserRole;
import com.foodey.server.user.UserService;
import com.foodey.server.utils.HttpHeaderUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final RefreshTokenRepository refreshTokenRepository;

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final JwtService jwtService;

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
        .name(user.getUsername())
        .phoneNumber(user.getPhoneNumber())
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
                  return new RefreshTokenException(refreshToken, "Invalid refresh token");
                });

    if (savedRefreshToken.isRevoked()) {
      throw new RefreshTokenException(refreshToken, "Refresh token revoked");
    } else if (savedRefreshToken.isExpired()) {
      refreshTokenRepository.save(savedRefreshToken.revoke());
      throw new RefreshTokenException(refreshToken, "Refresh token expired");
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
  public void upgradeRole(NewRoleRequest request, User user) throws BindException {
    UserRole userRole = new SellerRoleDecorator(user, request);

    User updatedUser = userRole.upgradeRole();

    userService.save(updatedUser);
  }

  @Override
  @Transactional
  public void register(RegistrationRequest request) {
    final String phoneNumber = request.getPhoneNumber();

    if (userService.existsByPhoneNumber(phoneNumber))
      throw new ResourceAlreadyInUseException("User", "phoneNumber", phoneNumber);

    User user = userService.createBasicUser(request);

    userService.save(user);
  }
}
