package com.foodey.server.auth;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/** TokenRepository */
@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
  Optional<RefreshToken> findByToken(String token);

  int deleteByToken(String token);
}
