package com.foodey.server.admin;

import com.foodey.server.user.enums.RoleType;
import com.foodey.server.user.model.decorator.NewRoleRequest;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RolesAllowed(RoleType.Fields.ADMIN)
public class AdminController {

  private final AdminService adminService;

  @PostMapping("/approval/roles/{requestId}")
  public void approveNewRoleRequest(
      @PathVariable(name = "requestId", required = true) String requestId) {

    adminService.approveNewRoleRequest(requestId);
  }

  @DeleteMapping("/approval/roles/{requestId}")
  public void rejectNewRoleRequest(
      @PathVariable(name = "requestId", required = true) String requestId) {
    adminService.rejectNewRoleRequest(requestId);
  }

  @GetMapping("/approval/roles")
  public Slice<NewRoleRequest> getNewRoleRequests(
      @PageableDefault(page = 0, size = 12, sort = "createdAt", direction = Direction.ASC)
          Pageable pageable) {
    return adminService.getNewRoleRequests(pageable);
  }
}
