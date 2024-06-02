package com.foodey.server.shop.controller;

import com.foodey.server.annotation.CurrentUser;
import com.foodey.server.annotation.PublicEndpoint;
import com.foodey.server.shop.model.ShopBrand;
import com.foodey.server.shop.service.ShopBrandService;
import com.foodey.server.user.enums.RoleType;
import com.foodey.server.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shop-brands")
@RequiredArgsConstructor
public class ShopBrandController {

  private final ShopBrandService shopBrandService;

  @Operation(summary = "Create a new shop brand for seller")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Shop brand created"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "User is not a seller"),
    @ApiResponse(
        responseCode = "409",
        description = "Shop brand with the same name already exists"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping({"", "/"})
  @ResponseStatus(HttpStatus.CREATED)
  @RolesAllowed(RoleType.Fields.SELLER)
  public ShopBrand createShopBrand(
      @RequestBody @Valid ShopBrand shopBrand, @CurrentUser User user) {
    return shopBrandService.createShopBrand(shopBrand, user);
  }

  @Operation(summary = "Get shop brand by id")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shop brand found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "404", description = "Shop brand not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/{id}")
  @PublicEndpoint
  @ResponseStatus(HttpStatus.OK)
  public ShopBrand findById(@PathVariable(required = true, name = "id") String id) {
    return shopBrandService.findById(id);
  }

  @Operation(summary = "Get shop brands of the current seller")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shop brands found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "404", description = "Shop brand not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/me")
  @RolesAllowed(RoleType.Fields.SELLER)
  @ResponseStatus(HttpStatus.OK)
  public List<ShopBrand> findByOwnerId(@CurrentUser User user) {
    return shopBrandService.findByOwnerId(user.getId());
  }

  @Operation(summary = "Get all brands")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shop brands found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "404", description = "Shop brand not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping({"/", ""})
  @PublicEndpoint
  @ResponseStatus(HttpStatus.OK)
  public Slice<ShopBrand> findAll(
      @PageableDefault(page = 0, size = 12, sort = "name", direction = Direction.ASC)
          Pageable pageable) {
    return shopBrandService.findAll(pageable);
  }
}
