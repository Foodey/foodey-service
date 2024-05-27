package com.foodey.server.auth.fido2;

import com.foodey.server.user.model.User;
import com.webauthn4j.springframework.security.WebAuthnRegistrationRequestValidationResponse;
import com.webauthn4j.springframework.security.WebAuthnRegistrationRequestValidator;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecord;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecordImpl;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecordManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebAuthnServiceImpl implements WebAuthnService {

  // private UserDetailsManager userDetailsManager;

  private final WebAuthnCredentialRecordManager webAuthnAuthenticatorManager;

  private final WebAuthnRegistrationRequestValidator registrationRequestValidator;

  // private final ChallengeRepository challengeRepository;

  // private final PasswordEncoder passwordEncoder;

  // private final AuthenticationTrustResolver authenticationTrustResolver =
  //     new AuthenticationTrustResolverImpl();
  // public PublicKeyCredentialCreationOptions registerGenerateOptions(
  //     HttpServletRequest request,
  //     PublicKeyCredentialRequestOptions publicKeyCredentialRequestOptions) {
  //   return null;
  // }

  @Override
  public boolean signUp(HttpServletRequest request, WebAuthnCreationForm form) {
    WebAuthnRegistrationRequestValidationResponse registrationRequestValidationResponse =
        registrationRequestValidator.validate(
            request,
            form.getClientData(),
            form.getAttestationObject(),
            form.getTransports(),
            form.getClientExtensions());

    String username = form.getUsername();

    User user = new User();

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

    new InMemoryUserDetailsManager().createUser(user);
    webAuthnAuthenticatorManager.createCredentialRecord(authenticator);

    return true;
  }
}
