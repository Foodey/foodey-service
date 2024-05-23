package com.foodey.server.user.controller;

import com.foodey.server.annotation.CurrentUser;
import com.foodey.server.user.enums.RoleType;
import com.foodey.server.user.model.User;
import com.foodey.server.user.model.decorator.SellerRoleRequest;
import com.foodey.server.user.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  @PostMapping("/role/seller")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RolesAllowed(RoleType.Fields.CUSTOMER)
  public void registerSellerRole(
      @CurrentUser User user, @RequestBody @Valid SellerRoleRequest request) {
    userService.requestNewRole(user, request);
  }
}
