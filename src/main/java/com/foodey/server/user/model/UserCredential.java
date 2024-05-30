package com.foodey.server.user.model;

import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "user_credentials")
public class UserCredential {
  private String userId;

  private String credentialId;

  private WebAuthnCredentialRecord webAuthnCredentialRecord;
}
