package com.foodey.server.auth.fido2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webauthn4j.springframework.security.WebAuthnRegistrationRequestValidationResponse;
import com.webauthn4j.springframework.security.WebAuthnRegistrationRequestValidator;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecord;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecordImpl;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecordManager;
import com.webauthn4j.springframework.security.exception.BadOriginException;
import com.webauthn4j.springframework.security.util.internal.ServletUtil;
import com.webauthn4j.util.Base64UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebAuthnServiceImpl implements WebAuthnService {

  // private UserDetailsManager userDetailsManager;

  private final WebAuthnCredentialRecordManager webAuthnAuthenticatorManager;

  private final WebAuthnRegistrationRequestValidator registrationRequestValidator;

  private final ObjectMapper objectMapper;

  // private final ChallengeRepository challengeRepository;

  // private final PasswordEncoder passwordEncoder;

  // private final AuthenticationTrustResolver authenticationTrustResolver =
  //     new AuthenticationTrustResolverImpl();

  private void validateApkKeyHashOrigin(String origin) {
    if (origin.startsWith("android:apk-key-hash")
        && !origin.equals("android:apk-key-hash:-sYXRdwJA3hvue3mKpYrOZ9zSPC7b4mbgzJmdZEDO5w")) {
      throw new BadOriginException("APK key hash is not valid: " + origin);
    }
  }

  @SneakyThrows
  private String getClientDataBase64url(
      HttpServletRequest request, WebAuthnRegistrationRequest webAuthnRegistrationRequest) {
    @SuppressWarnings("unchecked")
    Map<String, Object> clientDataObject =
        objectMapper.readValue(
            new String(
                Base64UrlUtil.decode(webAuthnRegistrationRequest.getClientDataBase64url()),
                StandardCharsets.UTF_8),
            Map.class);

    String origin = (String) clientDataObject.get("origin");
    validateApkKeyHashOrigin(origin);

    clientDataObject.put("origin", ServletUtil.getOrigin(request).toString());
    String clientDataBase64url =
        Base64UrlUtil.encodeToString(objectMapper.writeValueAsBytes(clientDataObject));
    return clientDataBase64url;
  }

  @Override
  @SneakyThrows
  public boolean signUp(
      HttpServletRequest request, WebAuthnRegistrationRequest webAuthnRegistrationRequest) {
    // System.out.println(
    //     "clientDataObject: "
    //         + new String(
    //
    // Base64UrlUtil.decode(webAuthnRegistrationRequest.getAttestationObjectBase64url()),
    //             StandardCharsets.UTF_8));

    WebAuthnRegistrationRequestValidationResponse registrationRequestValidationResponse =
        registrationRequestValidator.validate(
            request,
            getClientDataBase64url(request, webAuthnRegistrationRequest),
            webAuthnRegistrationRequest.getAttestationObjectBase64url(),
            webAuthnRegistrationRequest.getTransports(),
            webAuthnRegistrationRequest.getClientExtensionsJSON());

    String username = webAuthnRegistrationRequest.getUsername();

    System.out.println("username: " + username);

    // User user = new User();

    WebAuthnCredentialRecord authenticator =
        new WebAuthnCredentialRecordImpl(
            "authenticator",
            username,
            registrationRequestValidationResponse
                .getAttestationObject()
                .getAuthenticatorData()
                .getAttestedCredentialData(),
            registrationRequestValidationResponse.getAttestationObject().getAttestationStatement(),
            registrationRequestValidationResponse
                .getAttestationObject()
                .getAuthenticatorData()
                .getSignCount(),
            registrationRequestValidationResponse.getTransports(),
            registrationRequestValidationResponse.getRegistrationExtensionsClientOutputs(),
            registrationRequestValidationResponse
                .getAttestationObject()
                .getAuthenticatorData()
                .getExtensions());

    // new InMemoryUserDetailsManager().createUser(user);
    // webAuthnAuthenticatorManager.createCredentialRecord(authenticator);

    return true;
  }
}
