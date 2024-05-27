package com.foodey.server.auth.fido2;

import com.esotericsoftware.kryo.serializers.FieldSerializer.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
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
  @NotBlank private String username;

  private String password;

  @NotNull private String clientDataBase64url;

  @NotNull private String attestationObjectBase64url;

  @NotNull private Set<String> transports;

  @NotNull private String clientExtensionsJSON;

  @JsonProperty("response")
  private void unpackResponse(Map<String, Object> response) {
    this.clientDataBase64url = (String) response.get("clientDataJSON");
    this.attestationObjectBase64url = (String) response.get("attestationObject");
    if (transports instanceof Set) {
      this.transports = (Set<String>) transports;
    } else if (transports instanceof List) {
      this.transports = Set.copyOf((List<String>) transports);
    }
  }

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
