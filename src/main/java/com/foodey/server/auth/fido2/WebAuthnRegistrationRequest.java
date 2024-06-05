package com.foodey.server.auth.fido2;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodey.server.validation.annotation.OptimizedName;
import com.foodey.server.validation.annotation.PhoneNumber;
import com.webauthn4j.data.PublicKeyCredentialType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebAuthnRegistrationRequest {
  @OptimizedName private String name;

  @PhoneNumber
  @JsonAlias({"username", "phoneNumber"})
  private String phoneNumber;

  private String password = NanoIdUtils.randomNanoId();

  private String id;

  private String rawId;

  // private String userPubId;
  private byte[] userPubId;

  // @JsonProperty("userPubId")
  // public void convertUserPubId(String userPubId) {
  //   byte[] userPubIdBytes = Base64UrlUtil.decode(userPubId);

  //   // this.

  //   // this.userPubId = userPubId.getBytes();
  // }

  private PublicKeyCredentialType type;

  private String authenticatorFriendlyName = "authenticator";

  @Schema(description = "The response from the authenticator")
  @NotNull
  private Map<String, Object> response;

  // @JsonProperty("response")
  // @SuppressWarnings("unchecked")
  // private void unpackResponse(Map<String, Object> response) {
  //   this.response = response;
  //   this.clientDataBase64url = (String) response.get("clientDataJSON");
  //   this.attestationObjectBase64url = (String) response.get("attestationObject");
  //   if (transports instanceof Set) {
  //     this.transports = (Set<String>) transports;
  //   } else if (transports instanceof List) {
  //     this.transports = Set.copyOf((List<String>) transports);
  //   }
  // }

  public String getClientDataBase64url() {
    return (String) response.get("clientDataJSON");
  }

  public String getAttestationObjectBase64url() {
    return (String) response.get("attestationObject");
  }

  @SuppressWarnings("unchecked")
  public Set<String> getTransports() {
    Object transports = response.get("transports");
    if (transports instanceof Set) {
      return (Set<String>) transports;
    } else {
      return Set.copyOf((List<String>) transports);
    }
  }

  // @Schema(
  //     description =
  //         "The client data in base64url encoding. It is the  unpack clientDataJSON of the
  // response")
  // @NotNull
  // private String clientDataBase64url;

  // @Schema(
  //     description =
  //         "The attestation object in base64url encoding. It is the unpack attestationObject of
  // the"
  //             + " response")
  // @NotNull
  // private String attestationObjectBase64url;

  // @Schema(
  //     description =
  //         "The transports supported by the authenticator. This is the unpack transports of the"
  //             + " response")
  // @NotNull
  // private Set<String> transports;

  private String clientExtensionsJSON;

  @JsonProperty("clientExtensionResults")
  private void unpackClientExtensions(Object clientExtensions) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      this.clientExtensionsJSON = mapper.writeValueAsString(clientExtensions);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
