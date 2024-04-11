package com.foodey.server.user;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByPhoneNumber(String phoneNumber);

  Optional<User> findByPubId(String pubId);

  boolean existsByPhoneNumber(String phoneNumber);
}
