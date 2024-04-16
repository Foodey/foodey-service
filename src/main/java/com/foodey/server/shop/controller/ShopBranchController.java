package com.foodey.server.shop.controller;

import com.foodey.server.annotation.CurrentUser;
import com.foodey.server.shop.model.ShopBranch;
import com.foodey.server.shop.service.ShopBranchService;
import com.foodey.server.user.model.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @PostMapping({"", "/"})
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('SELLER')")
  public ShopBranch createShopBranch(
      @RequestBody @Valid ShopBranch shopBranch, @CurrentUser User user) {
    return shopBranchService.createShopBranch(shopBranch, user);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ShopBranch findById(@PathVariable(required = true, name = "id") String id) {
    return shopBranchService.findById(id);
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('SELLER')")
  @ResponseStatus(HttpStatus.OK)
  public List<ShopBranch> findByOwnerId(@CurrentUser User user) {
    return shopBranchService.findByOwnerId(user.getId());
  }
}
