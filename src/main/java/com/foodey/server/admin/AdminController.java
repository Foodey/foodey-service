package com.foodey.server.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  private final AdminService adminService;

  @PostMapping("/approval/roles/{requestId}")
  public void approveNewRoleRequest(
      @PathVariable(name = "requestId", required = true) String requestId) {

    adminService.approveNewRoleRequest(requestId);
  }
}
