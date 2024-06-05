package com.foodey.server.auth.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.foodey.server.exceptions.HttpRequestException;
import com.foodey.server.user.model.User;
import com.foodey.server.user.repository.UserRepository;
import com.foodey.server.utils.HttpServletRequestUtils;
import com.webauthn4j.data.PublicKeyCredentialUserEntity;
import com.webauthn4j.springframework.security.options.PublicKeyCredentialUserEntityProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicKeyCredentialUserEntityProviderImpl
    implements PublicKeyCredentialUserEntityProvider {

  @Value("${foodey.webauthn.user.name}")
  private String USER_NAME;

  @Value("${foodey.webauthn.user.displayName}")
  private String DISPLAY_NAME;

  private final UserRepository userRepository;

  @Override
  @SneakyThrows
  public PublicKeyCredentialUserEntity provide(Authentication authentication) {
    HttpServletRequest request =
        HttpServletRequestUtils.getRequest()
            .orElseThrow(
                () ->
                    new HttpRequestException(
                        HttpMethod.GET, HttpStatus.NOT_ACCEPTABLE, "Request not found."));

    if (request.getMethod().equals("POST")) {
      if (authentication != null) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
          User user = (User) principal;
          return new PublicKeyCredentialUserEntity(
              user.getPubId().getBytes(StandardCharsets.UTF_8), user.getUsername(), user.getName());
        } else {
          String username = authentication.getName();
          return new PublicKeyCredentialUserEntity(
              username.getBytes(StandardCharsets.UTF_8), username, username);
        }
      }

      String phoneNumber = request.getParameter("phoneNumber");
      if (StringUtils.hasText(phoneNumber)) {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElse(null);
        if (user != null) {
          log.info("Trying to create new credential for existing user: {}", user.getUsername());
          return new PublicKeyCredentialUserEntity(
              user.getPubId().getBytes(StandardCharsets.UTF_8), user.getUsername(), user.getName());
        }

        String pubId = NanoIdUtils.randomNanoId();
        log.info("Trying to create new user with phone number: {}", phoneNumber);
        return new PublicKeyCredentialUserEntity(pubId.getBytes(), phoneNumber, DISPLAY_NAME);

      } else {
        throw new HttpRequestException(
            HttpMethod.POST, HttpStatus.NOT_ACCEPTABLE, "Missing phone number in request params.");
      }
    }

    throw new HttpRequestException(
        HttpMethod.GET, HttpStatus.NOT_ACCEPTABLE, "GET request is not allowed.");
  }
}
