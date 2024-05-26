package com.foodey.server.auth.fido2;

import org.springframework.stereotype.Service;

@Service
public class WebAuthnService {
  // private final Map<String, User> users = new HashMap<>();
  // private final Map<String, PublicKeyCredentialCreationOptions> registrationOptions =
  //     new HashMap<>();
  // private final Map<String, PublicKeyCredentialRequestOptions> authenticationOptions =
  //     new HashMap<>();

  // public PublicKeyCredentialCreationOptions startRegistration(User user) {
  //   RelyingPartyIdentity rp =
  //       RelyingPartyIdentity.builder().id("example.com").name("Example").build();

  //   UserIdentity userIdentity =
  //       UserIdentity.builder()
  //           .name(user.getUsername())
  //           .displayName(user.getDisplayName())
  //           .id(user.getId().getBytes())
  //           .build();

  //   Challenge challenge = new DefaultChallenge();

  //   PublicKeyCredentialCreationOptions options =
  //       PublicKeyCredentialCreationOptions.builder()
  //           .rp(rp)
  //           .user(userIdentity)
  //           .challenge(challenge)
  //           .pubKeyCredParams(
  //               Arrays.asList(
  //                   new PublicKeyCredentialParameters(
  //                       PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256)))
  //           .build();

  //   users.put(user.getId(), user);
  //   registrationOptions.put(user.getId(), options);
  //   return options;
  // }

  // public void finishRegistration(
  //     PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs>
  //         credential) {
  //   AuthenticatorAttestationResponse response = credential.getResponse();
  //   byte[] clientDataJSON = response.getClientDataJSON();
  //   byte[] attestationObject = response.getAttestationObject();

  //   // Xác minh attestation và lưu trữ khóa công khai và các thông tin liên quan
  //   // ...

  //   // Lưu thông tin xác thực cho người dùng
  //   User user = users.get(new String(response.getClientData().getUserHandle()));
  //   user.addCredential(credential);
  // }

  // public PublicKeyCredentialRequestOptions startAuthentication() {
  //   RelyingPartyIdentity rp =
  //       RelyingPartyIdentity.builder().id("example.com").name("Example").build();

  //   Challenge challenge = new DefaultChallenge();

  //   PublicKeyCredentialRequestOptions options =
  //       PublicKeyCredentialRequestOptions.builder()
  //           .rpId(rp.getId())
  //           .challenge(challenge)
  //           .allowCredentials(
  //               users.values().stream()
  //                   .flatMap(user -> user.getCredentials().stream())
  //                   .map(
  //                       cred ->
  //                           PublicKeyCredentialDescriptor.builder()
  //                               .id(cred.getRawId())
  //                               .type(PublicKeyCredentialType.PUBLIC_KEY)
  //                               .build())
  //                   .collect(Collectors.toList()))
  //           .build();

  //   // Lưu lại các tùy chọn cho lần xác thực
  //   authenticationOptions.put(rp.getId(), options);
  //   return options;
  // }

  // public void finishAuthentication(
  //     PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs>
  //         credential) {
  //   AuthenticatorAssertionResponse response = credential.getResponse();
  //   byte[] clientDataJSON = response.getClientDataJSON();
  //   byte[] authenticatorData = response.getAuthenticatorData();
  //   byte[] signature = response.getSignature();
  //   byte[] userHandle = response.getUserHandle();

  //   // Xác minh assertion và hoàn tất xác thực
  //   // ...
  // }
}
