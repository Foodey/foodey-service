// package com.foodey.server.auth.fido2;

// import com.foodey.server.exceptions.ResourceNotFoundException;
// import com.foodey.server.user.model.User;
// import com.foodey.server.user.model.UserCredentialRecord;
// import com.foodey.server.user.repository.UserCredentialRecordRepository;
// import com.foodey.server.user.repository.UserRepository;
// import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecord;
// import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecordService;
// import com.webauthn4j.springframework.security.exception.CredentialIdNotFoundException;
// import com.webauthn4j.util.Base64UrlUtil;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Service;

// @Service
// @RequiredArgsConstructor
// public class WebAuthnCredentialRecordManagerImpl implements WebAuthnCredentialRecordService {

//   private final UserCredentialRecordRepository userCredentialRecordRepository;
//   private final UserRepository userRepository;

//   @Override
//   public void updateCounter(byte[] credentialId, long counter)
//       throws CredentialIdNotFoundException {
//     UserCredentialRecord authenticatorEntity =
//         userCredentialRecordRepository
//             .findByCredentialId(Base64UrlUtil.encodeToString(credentialId))
//             .orElseThrow(() -> new CredentialIdNotFoundException("AuthenticatorEntity not
// found"));
//     authenticatorEntity.setCounter(counter);
//     userCredentialRecordRepository.save(authenticatorEntity);
//   }

//   @Override
//   public WebAuthnCredentialRecord loadCredentialRecordByCredentialId(byte[] credentialId) {
//     return userCredentialRecordRepository
//         .findByCredentialId(Base64UrlUtil.encodeToString(credentialId))
//         .orElseThrow(() -> new CredentialIdNotFoundException("AuthenticatorEntity not found"));
//   }

//   @Override
//   public List<WebAuthnCredentialRecord> loadCredentialRecordsByUserPrincipal(Object principal) {
//     String phoneNumber;
//     if (principal == null) {
//       return Collections.emptyList();
//     } else if (principal instanceof User) {
//       return new ArrayList<>(
//           userCredentialRecordRepository.findByUserId(((User) principal).getId()));
//     } else if (principal instanceof UserDetails) {
//       phoneNumber = ((UserDetails) principal).getUsername();
//     } else if (principal instanceof String) {
//       phoneNumber = (String) principal;
//     } else if (principal instanceof Authentication) {
//       phoneNumber = ((Authentication) principal).getName();
//     } else {
//       throw new IllegalArgumentException("unexpected principal is specified.");
//     }

//     User user =
//         userRepository
//             .findByPhoneNumber(phoneNumber)
//             .orElseThrow(() -> new ResourceNotFoundException("User", "phoneNumber",
// phoneNumber));
//     return new ArrayList<>(userCredentialRecordRepository.findByUserId(user.getId()));
//     // return new ArrayList<>(
//     //     userCredentialRecordRepository.findByUserId(user.getId()).stream()
//     //         .map(UserCredentialRecord::getWebAuthnCredentialRecord)
//     //         .toList());
//   }
// }
