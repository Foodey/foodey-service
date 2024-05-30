package com.foodey.server.auth.fido2;

import com.foodey.server.user.repository.UserRepository;
import com.webauthn4j.springframework.security.credential.InMemoryWebAuthnCredentialRecordManager;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecord;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecordManager;
import com.webauthn4j.springframework.security.exception.CredentialIdNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class WebAuthnCredentialRecordManagerImpl implements WebAuthnCredentialRecordManager {

  private final UserRepository userRepository;

  private WebAuthnCredentialRecordManager webAuthnAuthenticatorManager() {
    return new InMemoryWebAuthnCredentialRecordManager();
  }

  @Override
  public void updateCounter(byte[] credentialId, long counter)
      throws CredentialIdNotFoundException {
    return;
  }

  @Override
  public WebAuthnCredentialRecord loadCredentialRecordByCredentialId(byte[] credentialId)
      throws CredentialIdNotFoundException {
    return null;
    // String credentialIdBase64Url = Base64UrlUtil.encodeToString(credentialId);

    // User user =
    //     userRepository
    //         .findByAuthenticatorsCredentialId(credentialIdBase64Url)
    //         .orElseThrow(
    //             () ->
    //                 new ResourceNotFoundException(
    //                     "User", "authenticators.credentialId", credentialIdBase64Url));

    // return user.getAuthenticators().stream()
    //     .filter(auth -> auth.getCredentialId().equals(credentialIdBase64Url))
    //     .findFirst()
    //     .orElseThrow(() -> new CredentialIdNotFoundException("CredentialId not found."))
    //     .getWebAuthnCredentialRecord();
  }

  @Override
  public List<WebAuthnCredentialRecord> loadCredentialRecordsByUserPrincipal(Object principal) {
    return null;
    // User user = (User) principal;
    // return userRepository
    //     .findById(user.getId())
    //     .orElseThrow(() -> new ResourceNotFoundException("User", "id", user.getId()))
    //     .getAuthenticators()
    //     .stream()
    //     .map(auth -> auth.getWebAuthnCredentialRecord())
    //     .toList();
  }

  @Override
  public void createCredentialRecord(WebAuthnCredentialRecord webAuthnCredentialRecord) {
    // User user = (User) webAuthnCredentialRecord.getUserPrincipal();

    // // user.getAuthenticatorDatas().add(webAuthnCredentialRecord.getAttestedCredentialData())

    // userRepository.save(user);
  }

  @Override
  public void deleteCredentialRecord(byte[] credentialId) throws CredentialIdNotFoundException {
    // String base64UrlCredentialId = Base64UrlUtil.encodeToString(credentialId);
    // userRepository
    //     .findByAuthenticatorsCredentialId(base64UrlCredentialId)
    //     .ifPresent(
    //         user -> {
    //           user.getAuthenticatorDatas()
    //               .removeIf(auth -> auth.getCredentialId().equals(base64UrlCredentialId));
    //           userRepository.save(user);
    //         });
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  @Override
  public boolean credentialRecordExists(byte[] credentialId) {
    return true;
    // return userRepository
    //     .findByAuthenticatorsCredentialId(Base64UrlUtil.encodeToString(credentialId))
    //     .isPresent();
  }
}
