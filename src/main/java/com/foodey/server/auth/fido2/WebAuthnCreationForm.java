package com.foodey.server.auth.fido2;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebAuthnCreationForm {

  @NotBlank private String username;

  @NotBlank private String clientData;

  @NotBlank private String attestationObject;

  private Set<String> transports;

  @NotBlank private String clientExtensions;

  private String userHandle;
}
