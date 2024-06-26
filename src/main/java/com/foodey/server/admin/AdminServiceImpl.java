package com.foodey.server.admin;

import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.user.model.User;
import com.foodey.server.user.model.decorator.NewRoleRequest;
import com.foodey.server.user.repository.NewRoleRequestRepository;
import com.foodey.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final NewRoleRequestRepository newRoleRequestRepository;

  private final UserService userService;

  @Override
  public void approveNewRoleRequest(String requestId) {
    NewRoleRequest request =
        newRoleRequestRepository
            .findById(requestId)
            .orElseThrow(() -> new ResourceNotFoundException("NewRoleRequest", "id", requestId));

    User user =
        userService
            .findById(request.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

    userService.upgradeRole(user, request);

    newRoleRequestRepository.deleteById(requestId);
  }

  @Override
  public void rejectNewRoleRequest(String requestId) {
    newRoleRequestRepository.deleteById(requestId);
  }

  @Override
  public Slice<NewRoleRequest> getNewRoleRequests(Pageable pageable) {
    return newRoleRequestRepository.findAll(pageable);
  }
}
