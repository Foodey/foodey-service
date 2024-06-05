package com.foodey.server.user.repository;

import com.foodey.server.user.model.UserCredentialRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialRecordRepository
    extends MongoRepository<UserCredentialRecord, String> {

  boolean existsByCredentialId(String credentialId);

  Optional<UserCredentialRecord> findByCredentialId(@Param("credentialId") String credentialId);

  List<UserCredentialRecord> findByUserId(String userId);
}
