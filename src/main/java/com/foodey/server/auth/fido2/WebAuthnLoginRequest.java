package com.foodey.server.auth.fido2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webauthn4j.data.AuthenticatorAssertionResponse;
import com.webauthn4j.data.PublicKeyCredentialType;
import com.webauthn4j.util.Base64UrlUtil;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebAuthnLoginRequest {

  private String id;

  private byte[] credentialId;

  @JsonProperty("rawId")
  public void convertRawId(String rawId) {
    byte[] rawIdBytes = Base64UrlUtil.decode(rawId);
    this.credentialId = rawIdBytes;
  }

  private AuthenticatorAssertionResponse response;

  private String clientExtensionsJSON;

  @JsonProperty("clientExtensionResults")
  private void unpackClientExtensions(Object clientExtensionResults) {
    if (clientExtensionResults == null) return;
    try {
      this.clientExtensionsJSON = new ObjectMapper().writeValueAsString(clientExtensionResults);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @JsonProperty("response")
  public void convertResponse(Map<String, String> responseConverter) {
    String clientDataJSON = responseConverter.get("clientDataJSON");
    String authenticatorData = responseConverter.get("authenticatorData");
    String signature = responseConverter.get("signature");
    String userHandle = responseConverter.get("userHandle");

    byte[] rawClientDataJSON = Base64UrlUtil.decode(clientDataJSON);
    byte[] rawAuthenticatorData = Base64UrlUtil.decode(authenticatorData);
    byte[] signatureBytes = Base64UrlUtil.decode(signature);
    byte[] userHandleBytes = Base64UrlUtil.decode(userHandle);

    response =
        new AuthenticatorAssertionResponse(
            rawClientDataJSON, rawAuthenticatorData, signatureBytes, userHandleBytes);
  }

  private PublicKeyCredentialType type;
}
