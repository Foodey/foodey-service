package com.foodey.server.user.repository;

import com.foodey.server.user.enums.RoleType;
import com.foodey.server.user.model.decorator.NewRoleRequest;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NewRoleRequestRepository extends MongoRepository<NewRoleRequest, String> {

  Optional<NewRoleRequest> findByUserId(String userId);

  boolean existsByUserIdAndRole(String userId, RoleType role);
}
