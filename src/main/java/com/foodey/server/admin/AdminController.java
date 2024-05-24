package com.foodey.server.admin;

import com.foodey.server.user.enums.RoleType;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
// @PreAuthorize("hasRole('ADMIN')")
@RolesAllowed(RoleType.Fields.ADMIN)
public class AdminController {

  private final AdminService adminService;

  @PostMapping("/approval/roles/{requestId}")
  public void approveNewRoleRequest(
      @PathVariable(name = "requestId", required = true) String requestId) {

    adminService.approveNewRoleRequest(requestId);
  }
}
