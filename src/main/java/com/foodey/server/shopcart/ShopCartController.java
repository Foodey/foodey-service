package com.foodey.server.shopcart;

import com.foodey.server.annotation.CurrentUser;
import com.foodey.server.user.enums.RoleType;
import com.foodey.server.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopcarts/{shopId}")
public class ShopCartController {

  private final ShopCartService shopCartService;

  @PatchMapping("/products/{productId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RolesAllowed(RoleType.Fields.CUSTOMER)
  @Operation(summary = "Adjust the quantity of a product in the shop cart")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Product quantity adjusted successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
    @ApiResponse(
        responseCode = "403",
        description = "User does not have permission to access this resource"),
    @ApiResponse(responseCode = "404", description = "Product not found in the shop cart"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public void adjustProduct(
      @PathVariable(required = true, name = "shopId") String shopId,
      @PathVariable(required = true, name = "productId") String productId,
      @RequestParam(required = true, defaultValue = ShopCartProductAction.Fields.ADD_PRODUCT)
          ShopCartProductAction action,
      @RequestParam(required = true) @Valid @Min(value = 1) long quantity) {
    shopCartService.adjustProduct(shopId, productId, quantity, action);
  }

  @DeleteMapping("/products/{productId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RolesAllowed(RoleType.Fields.CUSTOMER)
  @Operation(summary = "Remove a product from the shop cart")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Product removed successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
    @ApiResponse(
        responseCode = "403",
        description = "User does not have permission to access this resource"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public void removeProduct(
      @PathVariable(required = true, name = "shopId") String shopId,
      @PathVariable(required = true, name = "productId") String productId) {
    shopCartService.removeProduct(shopId, productId);
  }

  @DeleteMapping({"/", ""})
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RolesAllowed(RoleType.Fields.CUSTOMER)
  @Operation(summary = "Clear the shop cart")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Shop cart cleared successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
    @ApiResponse(
        responseCode = "403",
        description = "User does not have permission to access this resource"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public void clearShopCart(String shopId, @CurrentUser User user) {
    shopCartService.delete(user.getId(), shopId);
  }

  @Operation(summary = "Get the shop cart details")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shop cart details found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
    @ApiResponse(
        responseCode = "403",
        description = "User does not have permission to access this resource"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping({"/", ""})
  public ShopCartDetail getShopCartDetails(
      @PathVariable(required = true, name = "shopId") String shopId) {

    return shopCartService.getDetail(shopId);
  }
}
