// package com.foodey.server.user.model;

// import com.webauthn4j.data.AuthenticatorTransport;
// import com.webauthn4j.data.attestation.AttestationObject;
// import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData;
// import com.webauthn4j.data.attestation.statement.AttestationStatement;
// import com.webauthn4j.data.client.CollectedClientData;
// import com.webauthn4j.data.extension.authenticator.AuthenticationExtensionsAuthenticatorOutputs;
// import com.webauthn4j.data.extension.authenticator.RegistrationExtensionAuthenticatorOutput;
// import com.webauthn4j.data.extension.client.AuthenticationExtensionsClientOutputs;
// import com.webauthn4j.data.extension.client.RegistrationExtensionClientOutput;
// import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecord;
// import java.beans.Transient;
// import java.util.Set;
// import lombok.Data;
// import lombok.Getter;
// import lombok.Setter;
// import org.springframework.data.mongodb.core.mapping.DBRef;
// import org.springframework.data.mongodb.core.mapping.Document;

// @Getter
// @Setter
// @Document(collection = "user_credential_records")
// @Data
// public class UserCredentialRecord implements WebAuthnCredentialRecord {

//   private String credentialId;

//   @DBRef private User user;

//   private String name;
//   private long counter;

//   private boolean uvInitialized;
//   private boolean backupEligible;
//   private boolean backedUp;

//   private Set<AuthenticatorTransport> transports;

//   private AttestedCredentialData attestedCredentialData;

//   private AttestationStatement attestationStatement;

//   private CollectedClientData clientData;

//   private AuthenticationExtensionsClientOutputs<RegistrationExtensionClientOutput>
// clientExtensions;

//   private AuthenticationExtensionsAuthenticatorOutputs<RegistrationExtensionAuthenticatorOutput>
//       authenticatorExtensions;

//   @Override
//   @Transient
//   public User getUserPrincipal() {
//     return user;
//   }

//   public void setName(String name) {
//     this.name = name;
//   }

//   public long getCounter() {
//     return counter;
//   }

//   public void setCounter(long counter) {
//     this.counter = counter;
//   }

//   @Override
//   public Set<AuthenticatorTransport> getTransports() {
//     return transports;
//   }

//   public void setTransports(Set<AuthenticatorTransport> transports) {
//     this.transports = transports;
//   }

//   public AttestedCredentialData getAttestedCredentialData() {
//     return attestedCredentialData;
//   }

//   public void setAttestedCredentialData(AttestedCredentialData attestedCredentialData) {
//     this.attestedCredentialData = attestedCredentialData;
//   }

//   public AttestationStatement getAttestationStatement() {
//     return attestationStatement;
//   }

//   public void setAttestationStatement(AttestationStatement attestationStatement) {
//     this.attestationStatement = attestationStatement;
//   }

//   @Override
//   public AuthenticationExtensionsClientOutputs<RegistrationExtensionClientOutput>
//       getClientExtensions() {
//     return clientExtensions;
//   }

//   public void setClientExtensions(
//       AuthenticationExtensionsClientOutputs<RegistrationExtensionClientOutput> clientExtensions)
// {
//     this.clientExtensions = clientExtensions;
//   }

//   @Override
//   public AuthenticationExtensionsAuthenticatorOutputs<RegistrationExtensionAuthenticatorOutput>
//       getAuthenticatorExtensions() {
//     return authenticatorExtensions;
//   }

//   public void setAuthenticatorExtensions(
//       AuthenticationExtensionsAuthenticatorOutputs<RegistrationExtensionAuthenticatorOutput>
//           authenticatorExtensions) {
//     this.authenticatorExtensions = authenticatorExtensions;
//   }

//   @Override
//   public CollectedClientData getClientData() {
//     return this.clientData;
//   }

//   public void setClientData(CollectedClientData clientData) {
//     this.clientData = clientData;
//   }

//   @Override
//   public Boolean isUvInitialized() {
//     return this.uvInitialized;
//   }

//   @Override
//   public void setUvInitialized(boolean value) {
//     this.uvInitialized = value;
//   }

//   @Override
//   public Boolean isBackupEligible() {
//     return this.backupEligible;
//   }

//   @Override
//   public void setBackupEligible(boolean value) {
//     this.backupEligible = value;
//   }

//   @Override
//   public Boolean isBackedUp() {
//     return this.backedUp;
//   }

//   @Override
//   public void setBackedUp(boolean value) {
//     this.backedUp = value;
//   }

//   public UserCredentialRecord() {}

//   public UserCredentialRecord(
//       String name,
//       User user,
//       AttestedCredentialData attestedCredentialData,
//       AttestationStatement attestationStatement,
//       long counter,
//       Set<AuthenticatorTransport> transports,
//       AuthenticationExtensionsClientOutputs<RegistrationExtensionClientOutput> clientExtensions,
//       AuthenticationExtensionsAuthenticatorOutputs<RegistrationExtensionAuthenticatorOutput>
//           authenticatorExtensions) {
//     this.setName(name);
//     this.user = user;
//     this.attestedCredentialData = attestedCredentialData;
//     this.attestationStatement = attestationStatement;
//     this.counter = counter;
//     this.transports = transports;
//     this.clientExtensions = clientExtensions;
//     this.authenticatorExtensions = authenticatorExtensions;
//     this.credentialId = attestedCredentialData.getCredentialId();
//     // this.credentialId =
// Base64UrlUtil.encodeToString(attestedCredentialData.getCredentialId());
//     this.uvInitialized = false;
//     this.backupEligible = false;
//     this.backedUp = false;
//   }

//   public UserCredentialRecord(
//       String name,
//       User user,
//       AttestationObject attestationObject,
//       CollectedClientData collectedClientData,
//       AuthenticationExtensionsClientOutputs<RegistrationExtensionClientOutput> clientExtensions,
//       Set<AuthenticatorTransport> transports) {
//     this.name = name;
//     this.user = user;
//     this.attestationStatement = attestationObject.getAttestationStatement();
//     this.uvInitialized = false;
//     this.backupEligible = false;
//     this.backedUp = false;
//     this.counter = 0;
//     this.attestedCredentialData =
//         attestationObject.getAuthenticatorData().getAttestedCredentialData();
//     this.authenticatorExtensions = attestationObject.getAuthenticatorData().getExtensions();
//     this.clientData = collectedClientData;
//     this.clientExtensions = clientExtensions;
//     this.transports = transports;
//     this.credentialId =
//         attestationObject.getAuthenticatorData().getAttestedCredentialData().getCredentialId();
//     // this.credentialId =
//     //     Base64UrlUtil.encodeToString(
//     //
//     // attestationObject.getAuthenticatorData().getAttestedCredentialData().getCredentialId());
//   }
//   // /**
//   //  * Constructor
//   //  *
//   //  * @param name authenticator's friendly name
//   //  * @param userPrincipal principal that represents user
//   //  * @param attestationObject attestation object
//   //  * @param clientData client data
//   //  * @param clientExtensions client extensions
//   //  * @param transports transports
//   //  */
//   // public UserCredentialRecord(
//   //     String name,
//   //     User userPrincipal,
//   //     AttestationObject attestationObject,
//   //     CollectedClientData clientData,
//   //     AuthenticationExtensionsClientOutputs<RegistrationExtensionClientOutput>
// clientExtensions,
//   //     Set<AuthenticatorTransport> transports) {
//   //   super(name, null, attestationObject, clientData, clientExtensions, transports);
//   //   this.credentialId =
//   //       Base64UrlUtil.encodeToString(
//   //
//   // attestationObject.getAuthenticatorData().getAttestedCredentialData().getCredentialId());

//   //   this.user = userPrincipal;
//   // }

//   // /**
//   //  * Constructor
//   //  *
//   //  * @param name authenticator's friendly name
//   //  * @param userPrincipal principal that represents user
//   //  * @param uvInitialized uv initialized
//   //  * @param backupEligible backup eligible
//   //  * @param backupState backup state
//   //  * @param counter counter
//   //  * @param attestedCredentialData attested credential data
//   //  * @param authenticatorExtensions authenticator extensions
//   //  * @param clientData client data
//   //  * @param clientExtensions client extensions
//   //  * @param transports transports
//   //  */
//   // public UserCredentialRecord(
//   //     String name,
//   //     User userPrincipal,
//   //     AttestationStatement attestationStatement,
//   //     Boolean uvInitialized,
//   //     Boolean backupEligible,
//   //     Boolean backupState,
//   //     long counter,
//   //     AttestedCredentialData attestedCredentialData,
//   //     AuthenticationExtensionsAuthenticatorOutputs<RegistrationExtensionAuthenticatorOutput>
//   //         authenticatorExtensions,
//   //     CollectedClientData clientData,
//   //     AuthenticationExtensionsClientOutputs<RegistrationExtensionClientOutput>
// clientExtensions,
//   //     Set<AuthenticatorTransport> transports) {
//   //   super(
//   //       name,
//   //       null,
//   //       attestationStatement,
//   //       uvInitialized,
//   //       backupEligible,
//   //       backupState,
//   //       counter,
//   //       attestedCredentialData,
//   //       authenticatorExtensions,
//   //       clientData,
//   //       clientExtensions,
//   //       transports);
//   //   this.credentialId =
// Base64UrlUtil.encodeToString(attestedCredentialData.getCredentialId());
//   //   this.user = userPrincipal;
//   // }

//   // public UserCredentialRecord() {
//   //   super(null, null, null, null, null, null);
//   // }

//   // /** {@inheritDoc} */
//   // @Override
//   // public boolean equals(Object o) {
//   //   if (this == o) return true;
//   //   if (o == null || getClass() != o.getClass()) return false;
//   //   if (!super.equals(o)) return false;
//   //   UserCredentialRecord that = (UserCredentialRecord) o;
//   //   return Objects.equals(this.getName(), that.getName());
//   // }

//   // /** {@inheritDoc} */
//   // @Override
//   // public int hashCode() {
//   //   return super.hashCode();
//   // }

//   // public UserCredentialRecord(String credentialId, User user) {
//   //   super(null, null, null, null, null, null);
//   //   this.user = user;
//   //   this.credentialId = credentialId;
//   // }

//   // @Override
//   // @Transient
//   // public User getUserPrincipal() {
//   //   return user;
//   // }
// }

package com.foodey.server.user.model;

import static com.webauthn4j.data.attestation.authenticator.AuthenticatorData.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webauthn4j.converter.CollectedClientDataConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.AuthenticatorTransport;
import com.webauthn4j.data.attestation.AttestationObject;
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData;
import com.webauthn4j.data.attestation.statement.AttestationStatement;
import com.webauthn4j.data.client.CollectedClientData;
import com.webauthn4j.data.extension.authenticator.AuthenticationExtensionsAuthenticatorOutputs;
import com.webauthn4j.data.extension.authenticator.RegistrationExtensionAuthenticatorOutput;
import com.webauthn4j.data.extension.client.AuthenticationExtensionsClientOutputs;
import com.webauthn4j.data.extension.client.RegistrationExtensionClientOutput;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecord;
import com.webauthn4j.util.Base64UrlUtil;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
// @NoArgsConstructor

@Document(collection = "user_credential_records")
public class UserCredentialRecord implements WebAuthnCredentialRecord {

  private static ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.findAndRegisterModules();
  }

  @Id private String credentialId;

  @DBRef private User user;

  private long counter;
  private Boolean uvInitialized;
  private Boolean backupEligible;
  private Boolean backupState;
  private CollectedClientData clientData;
  private AuthenticationExtensionsClientOutputs<RegistrationExtensionClientOutput> clientExtensions;
  private Set<AuthenticatorTransport> transports;

  private AttestedCredentialData attestedCredentialData;
  private AttestationStatement attestationStatement;
  private AuthenticationExtensionsAuthenticatorOutputs<RegistrationExtensionAuthenticatorOutput>
      authenticatorExtensions;

  public void setCounter(long counter) {
    this.counter = counter;
  }

  public UserCredentialRecord(CollectedClientData clientData) {
    this.clientData = clientData;
  }

  public void setClientData(CollectedClientData clientData) {
    this.clientData = clientData;
  }

  public void setClientData(String clientData) {

    try {
      ObjectConverter objectConverter = new ObjectConverter();
      final CollectedClientDataConverter converter =
          new CollectedClientDataConverter(objectConverter);
      this.clientData = converter.convert(clientData);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setAuthenticatorExtensions(
      AuthenticationExtensionsAuthenticatorOutputs<RegistrationExtensionAuthenticatorOutput>
          authenticatorExtensions) {
    this.authenticatorExtensions = authenticatorExtensions;
  }

  public void setAttestationStatement(AttestationStatement attestationStatement) {
    this.attestationStatement = attestationStatement;
  }

  public UserCredentialRecord(
      User user,
      AttestationObject attestationObject,
      CollectedClientData collectedClientData,
      AuthenticationExtensionsClientOutputs<RegistrationExtensionClientOutput> clientExtensions,
      Set<AuthenticatorTransport> transports) {
    this.attestationStatement = attestationObject.getAttestationStatement();
    this.counter = 0;
    this.attestedCredentialData =
        attestationObject.getAuthenticatorData().getAttestedCredentialData();
    this.authenticatorExtensions = attestationObject.getAuthenticatorData().getExtensions();
    this.clientData = collectedClientData;
    this.clientExtensions = clientExtensions;
    this.transports = transports;
    setCredentialId(
        attestationObject.getAuthenticatorData().getAttestedCredentialData().getCredentialId());
    this.user = user;

    this.uvInitialized = (attestationObject.getAuthenticatorData().getFlags() & BIT_UV) != 0;
    this.backupEligible = (attestationObject.getAuthenticatorData().getFlags() & BIT_BE) != 0;
    this.backupState = (attestationObject.getAuthenticatorData().getFlags() & BIT_BS) != 0;
  }

  public void setCredentialId(byte[] credentialId) {
    this.credentialId = Base64UrlUtil.encodeToString(credentialId);
  }

  public byte[] getCredentialIdBytes() {
    return Base64UrlUtil.decode(credentialId);
  }

  @Override
  public @Nullable Boolean isUvInitialized() {
    return this.uvInitialized;
  }

  @Override
  public void setUvInitialized(boolean value) {
    this.uvInitialized = value;
  }

  @Override
  public @Nullable Boolean isBackupEligible() {
    return this.backupEligible;
  }

  @Override
  public void setBackupEligible(boolean value) {
    this.backupEligible = value;
  }

  @Override
  public @Nullable Boolean isBackedUp() {
    return this.backupState;
  }

  @Override
  public void setBackedUp(boolean value) {
    this.backupState = value;
  }

  @Override
  public AttestedCredentialData getAttestedCredentialData() {
    return this.attestedCredentialData;
  }

  @Override
  public long getCounter() {
    return this.counter;
  }

  @Override
  public @Nullable CollectedClientData getClientData() {
    return this.clientData;
  }

  @Override
  public Object getUserPrincipal() {
    return user;
  }
}
