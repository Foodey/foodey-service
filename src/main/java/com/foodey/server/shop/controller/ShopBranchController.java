package com.foodey.server.shop.controller;

import com.foodey.server.annotation.CurrentUser;
import com.foodey.server.annotation.PublicEndpoint;
import com.foodey.server.shop.model.ShopBranch;
import com.foodey.server.shop.service.ShopBranchService;
import com.foodey.server.user.enums.RoleType;
import com.foodey.server.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shop-branches")
@RequiredArgsConstructor
public class ShopBranchController {

  private final ShopBranchService shopBranchService;

  @Operation(summary = "Create a new shop branch for seller")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Shop branch created"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "User is not a seller"),
    @ApiResponse(
        responseCode = "409",
        description = "Shop branch with the same name already exists"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping({"", "/"})
  @ResponseStatus(HttpStatus.CREATED)
  @RolesAllowed(RoleType.Fields.SELLER)
  public ShopBranch createShopBranch(
      @RequestBody @Valid ShopBranch shopBranch, @CurrentUser User user) {
    return shopBranchService.createShopBranch(shopBranch, user);
  }

  @Operation(summary = "Get shop branch by id")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shop branch found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "404", description = "Shop branch not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/{id}")
  @PublicEndpoint
  @ResponseStatus(HttpStatus.OK)
  public ShopBranch findById(@PathVariable(required = true, name = "id") String id) {
    return shopBranchService.findById(id);
  }

  @GetMapping("/user")
  @RolesAllowed(RoleType.Fields.SELLER)
  @ResponseStatus(HttpStatus.OK)
  public List<ShopBranch> findByOwnerId(@CurrentUser User user) {
    return shopBranchService.findByOwnerId(user.getId());
  }
}
