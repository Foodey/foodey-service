package com.foodey.server.auth.fido2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public class WebAuthnUserCreationForm {

  @NotNull private String userHandle;

  @NotEmpty private String username;

  @NotNull @Valid private String clientDataJSON;

  @NotNull @Valid private String attestationObject;

  private Set<String> transports;

  @NotNull private String clientExtensions;

  public String getUserHandle() {
    return userHandle;
  }

  public void setUserHandle(String userHandle) {
    this.userHandle = userHandle;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getClientDataJSON() {
    return clientDataJSON;
  }

  public void setClientDataJSON(String clientDataJSON) {
    this.clientDataJSON = clientDataJSON;
  }

  public String getAttestationObject() {
    return attestationObject;
  }

  public void setAttestationObject(String attestationObject) {
    this.attestationObject = attestationObject;
  }

  public Set<String> getTransports() {
    return transports;
  }

  public void setTransports(Set<String> transports) {
    this.transports = transports;
  }

  public String getClientExtensions() {
    return clientExtensions;
  }

  public void setClientExtensions(String clientExtensions) {
    this.clientExtensions = clientExtensions;
  }
}
