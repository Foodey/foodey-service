package com.foodey.server.user.controller;

import com.foodey.server.annotation.CurrentUser;
import com.foodey.server.product.model.FavoriteProduct;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.user.enums.RoleType;
import com.foodey.server.user.model.User;
import com.foodey.server.user.model.decorator.SellerRoleRequest;
import com.foodey.server.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RolesAllowed(RoleType.Fields.CUSTOMER)
public class UserController {

  private final UserService userService;

  @Operation(summary = "Request a seller role")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Role requested successfully"),
    @ApiResponse(responseCode = "208", description = "Role requested already sent"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "User is not allowed to perform this action"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping("/role/seller")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void registerSellerRole(
      @CurrentUser User user, @RequestBody @Valid SellerRoleRequest request) {
    userService.requestNewRole(user, request);
  }

  @Operation(summary = "Add a shop to the favorite list")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Shop added to the favorite list"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "User is not allowed to perform this action"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping("/favorite/shops/{shopId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void addFavoriteShop(
      @CurrentUser User user, @PathVariable(name = "shopId", required = true) String shopId) {
    userService.addFavoriteShop(user, shopId);
  }

  @Operation(summary = "Add a shop to the favorite list")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Shop added to the favorite list"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "User is not allowed to perform this action"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @DeleteMapping("/favorite/shops/{shopId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeFavoriteShop(
      @CurrentUser User user, @PathVariable(name = "shopId", required = true) String shopId) {
    userService.removeFavoriteShop(user, shopId);
  }

  @Operation(summary = "Add a product to the favorite list")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Product added to the favorite list"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "User is not allowed to perform this action"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping("/favorite/shops/{shopId}/products/{productId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void addFavoriteProduct(
      @CurrentUser User user,
      @PathVariable(name = "shopId", required = true) String shopId,
      @PathVariable(name = "productId", required = true) String productId) {
    userService.addFavoriteProduct(user, shopId, productId);
  }

  @Operation(summary = "Remove a product from the favorite list")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Product removed from the favorite list"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "User is not allowed to perform this action"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @DeleteMapping("/favorite/shops/{shopId}/products/{productId}")
  public void removeFavoriteProduct(
      @CurrentUser User user,
      @PathVariable(name = "shopId", required = true) String shopId,
      @PathVariable(name = "productId", required = true) String productId) {
    userService.removeFavoriteProduct(user, shopId, productId);
  }

  @Operation(summary = "Get favorite shops")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Favorite shops found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "User is not allowed to perform this action"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/favorite/shops")
  @ResponseStatus(HttpStatus.OK)
  public Slice<Shop> getFavoriteShops(
      @CurrentUser User user,
      @PageableDefault(page = 0, size = 12, sort = "createdAt", direction = Direction.ASC)
          Pageable pageable) {
    return userService.findFavoriteShops(user, pageable);
  }

  @Operation(summary = "Get favorite products")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Favorite products found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "User is not allowed to perform this action"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/favorite/products")
  @ResponseStatus(HttpStatus.OK)
  public Slice<FavoriteProduct> getFavoriteProducts(
      @CurrentUser User user,
      @PageableDefault(page = 0, size = 12, sort = "createdAt", direction = Direction.ASC)
          Pageable pageable) {
    return userService.findFavoriteProducts(user, pageable);
  }
}
