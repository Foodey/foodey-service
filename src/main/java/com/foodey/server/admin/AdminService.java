package com.foodey.server.admin;

import com.foodey.server.user.model.decorator.NewRoleRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface AdminService {

  void approveNewRoleRequest(String requestId);

  void rejectNewRoleRequest(String requestId);

  Slice<NewRoleRequest> getNewRoleRequests(Pageable pageable);
}
