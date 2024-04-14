package com.foodey.server.user.service;

import com.foodey.server.auth.dto.RegistrationRequest;
import com.foodey.server.user.model.User;
import com.foodey.server.user.model.decorator.NewRoleRequest;
import java.util.Optional;

public interface UserService {

  Optional<User> findByPhoneNumber(String phoneNumber);

  Optional<User> findById(String id);

  Optional<User> findByPubId(String pubId);

  User save(User user);

  boolean existsByPhoneNumber(String phoneNumber);

  User createBasicUser(RegistrationRequest registerRequest);

  void requestNewRole(User user, NewRoleRequest request);

  void upgradeRole(User user, NewRoleRequest request);
}
