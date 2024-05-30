package com.foodey.server.auth.fido2;

import com.esotericsoftware.kryo.serializers.FieldSerializer.NotNull;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodey.server.validation.annotation.PhoneNumber;
import com.webauthn4j.data.PublicKeyCredentialType;
import io.swagger.v3.oas.annotations.media.Schema;
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
  private String id;

  private String rawId;

  @PhoneNumber
  @JsonAlias({"username", "phoneNumber"})
  private String phoneNumber;

  private String password;

  private String name;

  private PublicKeyCredentialType type;

  @Schema(description = "The response from the authenticator")
  private Map<String, Object> response;

  @JsonProperty("response")
  @SuppressWarnings("unchecked")
  private void unpackResponse(Map<String, Object> response) {
    this.response = response;
    this.clientDataBase64url = (String) response.get("clientDataJSON");
    this.attestationObjectBase64url = (String) response.get("attestationObject");
    if (transports instanceof Set) {
      this.transports = (Set<String>) transports;
    } else if (transports instanceof List) {
      this.transports = Set.copyOf((List<String>) transports);
    }
  }

  @Schema(
      description =
          "The client data in base64url encoding. It is the  unpack clientDataJSON of the response")
  @NotNull
  private String clientDataBase64url;

  @Schema(
      description =
          "The attestation object in base64url encoding. It is the unpack attestationObject of the"
              + " response")
  @NotNull
  private String attestationObjectBase64url;

  @Schema(
      description =
          "The transports supported by the authenticator. This is the unpack transports of the"
              + " response")
  @NotNull
  private Set<String> transports;

  @NotNull private String clientExtensionsJSON;

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
