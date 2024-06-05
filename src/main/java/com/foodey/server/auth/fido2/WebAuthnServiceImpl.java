package com.foodey.server.auth.fido2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodey.server.auth.dto.JwtResponse;
import com.foodey.server.auth.dto.LoginResponse;
import com.foodey.server.auth.jwt.JwtService;
import com.foodey.server.auth.model.RefreshToken;
import com.foodey.server.auth.repository.RefreshTokenRepository;
import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.user.model.User;
import com.foodey.server.user.model.UserCredentialRecord;
import com.foodey.server.user.repository.UserCredentialRecordRepository;
import com.foodey.server.user.service.UserService;
import com.foodey.server.utils.ConsoleUtils;
import com.webauthn4j.data.attestation.AttestationObject;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.springframework.security.DefaultUserVerificationStrategy;
import com.webauthn4j.springframework.security.UserVerificationStrategy;
import com.webauthn4j.springframework.security.WebAuthnAuthenticationParameters;
import com.webauthn4j.springframework.security.WebAuthnAuthenticationRequest;
import com.webauthn4j.springframework.security.WebAuthnRegistrationRequestValidationResponse;
import com.webauthn4j.springframework.security.WebAuthnRegistrationRequestValidator;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecord;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecordManager;
import com.webauthn4j.springframework.security.exception.BadOriginException;
import com.webauthn4j.springframework.security.server.ServerPropertyProvider;
import com.webauthn4j.springframework.security.util.internal.ServletUtil;
import com.webauthn4j.util.Base64UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class WebAuthnServiceImpl implements WebAuthnService {

  private final UserCredentialRecordRepository userCredentialRecordRepository;
  private final WebAuthnRegistrationRequestValidator registrationRequestValidator;
  private final WebAuthnCredentialRecordManager webAuthnCredentialRecordManager;
  private final ObjectMapper objectMapper;
  private final UserService userService;
  private final JwtService jwtService;
  private final RefreshTokenRepository refreshTokenRepository;
  private final ServerPropertyProvider serverPropertyProvider;
  private final AuthenticationManager authenticationManager;
  private UserVerificationStrategy userVerificationStrategy = new DefaultUserVerificationStrategy();

  private void validateApkKeyHashOrigin(String origin) {
    if (origin.startsWith("android:apk-key-hash")
        && !origin.equals("android:apk-key-hash:-sYXRdwJA3hvue3mKpYrOZ9zSPC7b4mbgzJmdZEDO5w")) {
      throw new BadOriginException("APK key hash is not valid: " + origin);
    }
  }

  @SneakyThrows
  private String getClientData(HttpServletRequest request, String clientData) {
    @SuppressWarnings("unchecked")
    Map<String, Object> clientDataObject =
        objectMapper.readValue(
            new String(Base64UrlUtil.decode(clientData), StandardCharsets.UTF_8), Map.class);

    String origin = (String) clientDataObject.get("origin");
    validateApkKeyHashOrigin(origin);

    clientDataObject.put("origin", ServletUtil.getOrigin(request).toString());
    String clientDataBase64url =
        Base64UrlUtil.encodeToString(objectMapper.writeValueAsBytes(clientDataObject));
    return clientDataBase64url;
  }

  @Override
  @SneakyThrows
  public LoginResponse register(
      HttpServletRequest request, WebAuthnRegistrationRequest webAuthnRegistrationRequest) {

    WebAuthnRegistrationRequestValidationResponse registrationRequestValidationResponse =
        registrationRequestValidator.validate(
            request,
            getClientData(request, webAuthnRegistrationRequest.getClientDataBase64url()),
            webAuthnRegistrationRequest.getAttestationObjectBase64url(),
            webAuthnRegistrationRequest.getTransports(),
            webAuthnRegistrationRequest.getClientExtensionsJSON());

    String phoneNumber = webAuthnRegistrationRequest.getPhoneNumber();
    User user =
        userService
            .findByPhoneNumber(phoneNumber)
            .orElseGet(
                () -> {
                  User newUser =
                      userService.createBasicUser(
                          phoneNumber,
                          webAuthnRegistrationRequest.getPassword(),
                          webAuthnRegistrationRequest.getName());

                  String pubId =
                      new String(
                          webAuthnRegistrationRequest.getUserPubId(), StandardCharsets.UTF_8);
                  newUser.setPubId(pubId);
                  return userService.save(newUser);
                });

    UserCredentialRecord credentialRecord =
        createCredentialRecord(
            webAuthnRegistrationRequest.getAuthenticatorFriendlyName(),
            user,
            registrationRequestValidationResponse);

    if (!webAuthnCredentialRecordManager.credentialRecordExists(
        credentialRecord.getCredentialIdBytes())) {
      webAuthnCredentialRecordManager.createCredentialRecord(credentialRecord);
    } else {
      ConsoleUtils.prettyPrint("Credential already exists");
      throw new ResourceAlreadyInUseException(
          "UserCredentialRecord", "credentialId", credentialRecord.getCredentialId());
    }

    // if (!userCredentialRecordRepository.existsByCredentialId(credentialRecord.getCredentialId()))
    // {
    //   log.info("Saving credential record");
    //   userCredentialRecordRepository.save(credentialRecord);
    // } else {
    //   ConsoleUtils.prettyPrint("Credential already exists");
    //   throw new ResourceAlreadyInUseException(
    //       "UserCredentialRecord", "credentialId", credentialRecord.getCredentialId());
    // }

    final String accessToken = jwtService.generateAccessToken(user);
    final RefreshToken refreshToken = jwtService.generateRefreshToken(user);
    refreshTokenRepository.save(refreshToken);

    return LoginResponse.builder()
        .name(user.getName())
        .jwt(new JwtResponse(accessToken, refreshToken.getToken()))
        .build();
  }

  private UserCredentialRecord createCredentialRecord(
      String frendlyAuthenticatorName,
      User user,
      WebAuthnRegistrationRequestValidationResponse registrationRequestValidationResponse) {
    AttestationObject attestationObject =
        registrationRequestValidationResponse.getAttestationObject();
    // var authenticatorData = attestationObject.getAuthenticatorData();

    return new UserCredentialRecord(
        user,
        attestationObject,
        registrationRequestValidationResponse.getCollectedClientData(),
        registrationRequestValidationResponse.getRegistrationExtensionsClientOutputs(),
        registrationRequestValidationResponse.getTransports());
  }

  @Override
  @SneakyThrows
  public LoginResponse login(
      HttpServletRequest request, WebAuthnLoginRequest webAuthnLoginRequest) {

    ServerProperty serverProperty = serverPropertyProvider.provide(request);

    Map<String, Object> clientDataObject =
        objectMapper.readValue(webAuthnLoginRequest.getResponse().getClientDataJSON(), Map.class);
    String origin = (String) clientDataObject.get("origin");
    validateApkKeyHashOrigin(origin);
    clientDataObject.put("origin", ServletUtil.getOrigin(request).toString());

    WebAuthnAuthenticationRequest webAuthnAuthenticationRequest =
        new WebAuthnAuthenticationRequest(
            webAuthnLoginRequest.getCredentialId(),
            objectMapper.writeValueAsBytes(clientDataObject),
            // webAuthnLoginRequest.getResponse().getClientDataJSON(),
            webAuthnLoginRequest.getResponse().getAuthenticatorData(),
            webAuthnLoginRequest.getResponse().getSignature(),
            webAuthnLoginRequest.getClientExtensionsJSON());

    WebAuthnAuthenticationParameters webAuthnAuthenticationParameters =
        new WebAuthnAuthenticationParameters(
            serverProperty, userVerificationStrategy.isUserVerificationRequired(), true);

    // AbstractAuthenticationToken authenticationToken =
    //     new WebAuthnAssertionAuthenticationToken(
    //         webAuthnAuthenticationRequest, webAuthnAuthenticationParameters, null);

    WebAuthnCredentialRecord credentialRecord =
        webAuthnCredentialRecordManager.loadCredentialRecordByCredentialId(
            webAuthnLoginRequest.getCredentialId());

    // authenticationManager.authenticate(authenticationToken);

    User user = (User) credentialRecord.getUserPrincipal();

    final String accessToken = jwtService.generateAccessToken(user);
    final RefreshToken refreshToken = jwtService.generateRefreshToken(user);
    refreshTokenRepository.save(refreshToken);

    ConsoleUtils.prettyPrint("User logged in successfully");
    ConsoleUtils.prettyPrint(user);

    return LoginResponse.builder()
        .name(user.getName())
        .jwt(new JwtResponse(accessToken, refreshToken.getToken()))
        .build();
  }
}
