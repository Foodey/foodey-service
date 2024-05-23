package com.foodey.server.shop.controller;

import com.foodey.server.annotation.CurrentUser;
import com.foodey.server.annotation.PublicEndpoint;
import com.foodey.server.shop.model.MenuResponse;
import com.foodey.server.shop.model.ShopMenu;
import com.foodey.server.shop.service.ShopMenuService;
import com.foodey.server.user.enums.RoleType;
import com.foodey.server.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/shops/{shopId}/menus")
public class ShopMenuController {

  private final ShopMenuService shopMenuService;

  @Operation(summary = "Add new menu into shop with shopId")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Shop menu created successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(
        responseCode = "403",
        description = "User cannot access this resource. Only seller can access this resource"),
    @ApiResponse(responseCode = "404", description = "Shop not found"),
    @ApiResponse(responseCode = "409", description = "Menu with the same name already exists"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping({"", "/"})
  @ResponseStatus(HttpStatus.CREATED)
  @RolesAllowed(RoleType.Fields.SELLER)
  public ShopMenu addNewMenuIntoShop(
      @PathVariable(required = true, name = "shopId") String shopId,
      @Valid @RequestBody ShopMenu shopMenu,
      @CurrentUser User user) {
    return shopMenuService.createShopMenu(shopMenu, shopId, user);
  }

  @Operation(summary = "Get menu in shop with shopId. This is public endpoint.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shop menu found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "404", description = "Shop not found or menu not found in shop"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PublicEndpoint
  @GetMapping("/{menuId}")
  public ShopMenu getMenuInShop(
      @PathVariable(required = true, name = "shopId") String shopId,
      @PathVariable(required = true, name = "menuId") String menuId) {
    return shopMenuService.findMenuInShop(menuId, shopId).getValue();
  }

  @Operation(summary = "Get menu with products in shop with shopId. This is public endpoint.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shop menu found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "404", description = "Shop not found or menu not found in shop"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PublicEndpoint
  @GetMapping("/{menuId}/details")
  public MenuResponse getMenuDetailsInShop(
      @PathVariable(required = true, name = "shopId") String shopId,
      @PathVariable(required = true, name = "menuId") String menuId) {
    return shopMenuService.findMenuDetailsInShop(menuId, shopId);
  }

  @Operation(summary = "Get all menus in shop with shopId. This is public endpoint.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shop menus found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "404", description = "Shop not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PublicEndpoint
  @GetMapping({"", "/"})
  public Iterable<ShopMenu> getAllMenusInShop(
      @PathVariable(required = true, name = "shopId") String shopId) {
    return shopMenuService.findAllInShop(shopId);
  }

  @Operation(summary = "Get all menus with products in shop with shopId. This is public endpoint.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shop menus found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "404", description = "Shop not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PublicEndpoint
  @GetMapping("/details")
  public Iterable<MenuResponse> getAllMenusDetailsInShop(
      @PathVariable(required = true, name = "shopId") String shopId) {
    System.out.println("ShopMenuController.getAllMenusDetailsInShop");
    return shopMenuService.findAllDetailsInShop(shopId);
  }
}
