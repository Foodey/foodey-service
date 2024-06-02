package com.foodey.server.shop.controller;

import com.foodey.server.annotation.CurrentUser;
import com.foodey.server.annotation.PublicEndpoint;
import com.foodey.server.product.model.Product;
import com.foodey.server.shop.dto.MenuView;
import com.foodey.server.shop.service.MenuService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MenuController {

  private final MenuService menuService;

  @Operation(summary = "Add product to menu into shop")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Product added to menu successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(
        responseCode = "403",
        description = "User cannot access this resource. Only seller can access this resource"),
    @ApiResponse(responseCode = "404", description = "Shop not found"),
    @ApiResponse(responseCode = "409", description = "Menu with the same name already exists"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping("/shop-brands/{brandId}/shops/{shopId}/menu/products")
  @ResponseStatus(HttpStatus.CREATED)
  @RolesAllowed(RoleType.Fields.SELLER)
  public Product addProductToMenu(
      @PathVariable(required = true, name = "brandId") String brandId,
      @PathVariable(required = true, name = "shopId") String shopId,
      @Valid @RequestBody Product product,
      @RequestParam(defaultValue = "true") boolean appliedToAllShops,
      @CurrentUser User user) {
    return menuService.addProductToMenu(user, brandId, shopId, product, appliedToAllShops);
  }

  @Operation(summary = "Add products to menu into shop")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Products added to menu successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(
        responseCode = "403",
        description = "User cannot access this resource. Only seller can access this resource"),
    @ApiResponse(responseCode = "404", description = "Shop not found"),
    @ApiResponse(responseCode = "409", description = "Menu with the same name already exists"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping("/shop-brands/{brandId}/shops/{shopId}/menu/products/multiple")
  @ResponseStatus(HttpStatus.CREATED)
  @RolesAllowed(RoleType.Fields.SELLER)
  public List<Product> addProductsToMenu(
      @PathVariable(required = true, name = "brandId") String brandId,
      @PathVariable(required = true, name = "shopId") String shopId,
      @Valid @RequestBody List<@Valid Product> products,
      @RequestParam(defaultValue = "true") boolean appliedToAllShops,
      @CurrentUser User user) {
    return menuService.addProductsToMenu(user, brandId, shopId, products, appliedToAllShops);
  }

  @Operation(summary = "Get full menu details in shop")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shop menus found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "404", description = "Shop not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PublicEndpoint
  @GetMapping("/shop-brands/{brandId}/shops/{shopId}/menu/full")
  public MenuView getMenuDetailsInShop(
      @PathVariable(required = true, name = "brandId") String brandId,
      @PathVariable(required = true, name = "shopId") String shopId) {
    return menuService.getMenuInShop(brandId, shopId);
  }

  @Operation(summary = "Get menu details in shop by category name")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shop menus found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "404", description = "Shop not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PublicEndpoint
  @GetMapping("/shop-brands/{brandId}/shops/{shopId}/menu")
  public MenuView getMenusDetailsInShopByCategoryName(
      @PathVariable(required = true, name = "brandId") String brandId,
      @PathVariable(required = true, name = "shopId") String shopId,
      @RequestParam(required = true, name = "categoryName") String categoryName) {
    return menuService.getMenuInShop(brandId, shopId, categoryName);
  }

  @Operation(summary = "Get full menu details in brand")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Menu found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(
        responseCode = "403",
        description = "User cannot access this resource. Only seller can access this resource"),
    @ApiResponse(responseCode = "404", description = "Shop not found"),
    @ApiResponse(
        responseCode = "409",
        description = "There is already a menu that applied to all shops in the brand"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/shop-brands/{brandId}/menu/full")
  @PublicEndpoint
  @ResponseStatus(HttpStatus.CREATED)
  public MenuView getMenuInBrand(@PathVariable(required = true, name = "brandId") String brandId) {
    return menuService.getMenuInBrand(brandId);
  }

  @Operation(summary = "Get full menu details in brand")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Menu found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(
        responseCode = "403",
        description = "User cannot access this resource. Only seller can access this resource"),
    @ApiResponse(responseCode = "404", description = "Shop not found"),
    @ApiResponse(
        responseCode = "409",
        description = "There is already a menu that applied to all shops in the brand"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/shop-brands/{brandId}/menu")
  @PublicEndpoint
  @ResponseStatus(HttpStatus.CREATED)
  public MenuView getMenuInBrandByCategoryName(
      @PathVariable(required = true, name = "brandId") String brandId,
      @RequestParam(required = true) String categoryName) {
    return menuService.getMenuInBrand(brandId, categoryName);
  }
}
