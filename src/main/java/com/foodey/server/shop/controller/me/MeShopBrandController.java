package com.foodey.server.shop.controller.me;

import com.foodey.server.annotation.CurrentUser;
import com.foodey.server.shop.model.ShopBrand;
import com.foodey.server.shop.service.ShopBrandService;
import com.foodey.server.user.enums.RoleType;
import com.foodey.server.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/me/shop-brands")
@RolesAllowed(RoleType.Fields.SELLER)
public class MeShopBrandController {

  private final ShopBrandService shopBrandService;

  @Operation(summary = "Get shop brands of the current seller")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shop brands found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "404", description = "Shop brand not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public Slice<ShopBrand> findByOwnerId(
      @PageableDefault(page = 0, size = 12, sort = "name", direction = Direction.ASC)
          Pageable pageable,
      @CurrentUser User user) {
    return shopBrandService.findByOwnerId(user.getId(), pageable);
  }
}
