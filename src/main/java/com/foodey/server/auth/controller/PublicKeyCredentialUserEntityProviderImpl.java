package com.foodey.server.auth.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.webauthn4j.data.PublicKeyCredentialUserEntity;
import com.webauthn4j.springframework.security.options.PublicKeyCredentialUserEntityProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/** PublicKeyCredentialUserEntityProviderImpl */
@Service
public class PublicKeyCredentialUserEntityProviderImpl
    implements PublicKeyCredentialUserEntityProvider {

  @Value("${foodey.webauthn.user.name}")
  private String NAME;

  @Value("${foodey.webauthn.user.displayName}")
  private String DISPLAY_NAME;

  @Override
  public PublicKeyCredentialUserEntity provide(Authentication authentication) {
    return new PublicKeyCredentialUserEntity(
        NanoIdUtils.randomNanoId().getBytes(), NAME, DISPLAY_NAME);
  }
}
