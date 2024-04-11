package com.foodey.server.user;

import com.foodey.server.auth.RegistrationRequest;
import java.util.Optional;

public interface UserService {

  Optional<User> findByPhoneNumber(String phoneNumber);

  Optional<User> findById(String id);

  Optional<User> findByPubId(String pubId);

  User save(User user);

  boolean existsByPhoneNumber(String phoneNumber);

  User createBasicUser(RegistrationRequest registerRequest);
}
