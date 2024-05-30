package com.foodey.server.auth.fido2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodey.server.auth.dto.JwtResponse;
import com.foodey.server.utils.ConsoleUtils;
import com.webauthn4j.data.attestation.AttestationObject;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebAuthnServiceImpl implements WebAuthnService {
  private final WebAuthnCredentialRecordManager webAuthnAuthenticatorManager;

  private final WebAuthnRegistrationRequestValidator registrationRequestValidator;

  private final ObjectMapper objectMapper;

  private final PasswordEncoder passwordEncoder;

  //
  // private final UserRepository userRepository;

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
  public JwtResponse register(
      HttpServletRequest request, WebAuthnRegistrationRequest webAuthnRegistrationRequest) {

    WebAuthnRegistrationRequestValidationResponse registrationRequestValidationResponse =
        registrationRequestValidator.validate(
            request,
            getClientDataBase64url(request, webAuthnRegistrationRequest),
            webAuthnRegistrationRequest.getAttestationObjectBase64url(),
            webAuthnRegistrationRequest.getTransports(),
            webAuthnRegistrationRequest.getClientExtensionsJSON());

    String phoneNumber = webAuthnRegistrationRequest.getPhoneNumber();
    String password = webAuthnRegistrationRequest.getPassword();

    AttestationObject attestationObject =
        registrationRequestValidationResponse.getAttestationObject();
    var authenticatorData = attestationObject.getAuthenticatorData();

    ConsoleUtils.prettyPrint(attestationObject);

    WebAuthnCredentialRecord authenticator =
        new WebAuthnCredentialRecordImpl(
            "authenticator",
            phoneNumber,
            authenticatorData.getAttestedCredentialData(),
            attestationObject.getAttestationStatement(),
            authenticatorData.getSignCount(),
            registrationRequestValidationResponse.getTransports(),
            registrationRequestValidationResponse.getRegistrationExtensionsClientOutputs(),
            authenticatorData.getExtensions());

    webAuthnAuthenticatorManager.createCredentialRecord(authenticator);

    return null;
  }
}
