package com.foodey.server.user;

import com.foodey.server.auth.RegistrationRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public Optional<User> findByPhoneNumber(String phoneNumber) {
    return userRepository.findByPhoneNumber(phoneNumber);
  }

  public Optional<User> findById(String id) {
    return userRepository.findById(id);
  }

  public Optional<User> findByPubId(String pubId) {
    return userRepository.findByPubId(pubId);
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public boolean existsByPhoneNumber(String phoneNumber) {
    return userRepository.existsByPhoneNumber(phoneNumber);
  }

  public User createBasicUser(RegistrationRequest request) {
    return User.builder()
        .phoneNumber(request.getPhoneNumber())
        .password(passwordEncoder.encode(request.getPassword()))
        .name(request.getName())
        .build();
  }
}
