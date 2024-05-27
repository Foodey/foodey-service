package com.foodey.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.metadata.converter.jackson.WebAuthnMetadataJSONModule;
import com.webauthn4j.springframework.security.WebAuthnAuthenticationProvider;
import com.webauthn4j.springframework.security.WebAuthnRegistrationRequestValidator;
import com.webauthn4j.springframework.security.WebAuthnSecurityExpression;
import com.webauthn4j.springframework.security.challenge.ChallengeRepository;
import com.webauthn4j.springframework.security.challenge.HttpSessionChallengeRepository;
import com.webauthn4j.springframework.security.converter.jackson.WebAuthn4JSpringSecurityJSONModule;
import com.webauthn4j.springframework.security.credential.InMemoryWebAuthnCredentialRecordManager;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecordManager;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecordService;
import com.webauthn4j.springframework.security.options.AssertionOptionsProvider;
import com.webauthn4j.springframework.security.options.AssertionOptionsProviderImpl;
import com.webauthn4j.springframework.security.options.AttestationOptionsProvider;
import com.webauthn4j.springframework.security.options.AttestationOptionsProviderImpl;
import com.webauthn4j.springframework.security.options.RpIdProvider;
import com.webauthn4j.springframework.security.options.RpIdProviderImpl;
import com.webauthn4j.springframework.security.server.ServerPropertyProvider;
import com.webauthn4j.springframework.security.server.ServerPropertyProviderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebAuthnConfig {

  @Bean
  public WebAuthnCredentialRecordManager webAuthnAuthenticatorManager() {
    return new InMemoryWebAuthnCredentialRecordManager();
  }

  @Bean
  public WebAuthnAuthenticationProvider webAuthnAuthenticationProvider(
      WebAuthnCredentialRecordService authenticatorService, WebAuthnManager webAuthnManager) {
    return new WebAuthnAuthenticationProvider(authenticatorService, webAuthnManager);
  }

  @Bean
  public WebAuthnSecurityExpression webAuthnSecurityExpression() {
    return new WebAuthnSecurityExpression();
  }

  @Bean
  public ChallengeRepository challengeRepository() {
    return new HttpSessionChallengeRepository();
  }

  @Bean
  public AttestationOptionsProvider attestationOptionsProvider(
      RpIdProvider rpIdProvider,
      WebAuthnCredentialRecordService webAuthnCredentialRecordService,
      ChallengeRepository challengeRepository) {
    return new AttestationOptionsProviderImpl(
        rpIdProvider, webAuthnCredentialRecordService, challengeRepository);
  }

  @Bean
  public AssertionOptionsProvider assertionOptionsProvider(
      RpIdProvider rpIdProvider,
      WebAuthnCredentialRecordService webAuthnCredentialRecordService,
      ChallengeRepository challengeRepository) {
    return new AssertionOptionsProviderImpl(
        rpIdProvider, webAuthnCredentialRecordService, challengeRepository);
  }

  @Bean
  public RpIdProvider rpIdProvider() {
    return new RpIdProviderImpl();
  }

  @Bean
  public ServerPropertyProvider serverPropertyProvider(
      RpIdProvider rpIdProvider, ChallengeRepository challengeRepository) {
    return new ServerPropertyProviderImpl(rpIdProvider, challengeRepository);
  }

  @Bean
  public WebAuthnRegistrationRequestValidator webAuthnRegistrationRequestValidator(
      WebAuthnManager webAuthnManager, ServerPropertyProvider serverPropertyProvider) {
    return new WebAuthnRegistrationRequestValidator(webAuthnManager, serverPropertyProvider);
  }

  @Bean
  public ObjectConverter objectConverter() {
    ObjectMapper jsonMapper = new ObjectMapper();
    jsonMapper.registerModule(new WebAuthnMetadataJSONModule());
    jsonMapper.registerModule(new WebAuthn4JSpringSecurityJSONModule());

    ObjectMapper cborMapper = new ObjectMapper(new CBORFactory());
    return new ObjectConverter(jsonMapper, cborMapper);
  }

  @Bean
  public WebAuthnManager webAuthnManager(ObjectConverter objectConverter) {
    return WebAuthnManager.createNonStrictWebAuthnManager(objectConverter);
  }

  // @Bean
  // public PublicKeyCredentialUserEntityProvider publicKeyCredentialUserEntityProvider() {
  // }
}
