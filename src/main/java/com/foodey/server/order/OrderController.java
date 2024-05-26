package com.foodey.server.order;

import com.foodey.server.annotation.CurrentUser;
import com.foodey.server.user.enums.RoleType;
import com.foodey.server.user.model.User;
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
@RolesAllowed(RoleType.Fields.CUSTOMER)
@RequestMapping("/api/v1/orders")
public class OrderController {

  private final OrderService orderService;

  @Operation(summary = "Create an order from the shopping cart")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Order created successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "User is not allowed to perform this action"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping({"", "/"})
  @ResponseStatus(HttpStatus.CREATED)
  public Order createOrderFromShoppingCart(
      @RequestBody @Valid OrderRequest orderRequest, @CurrentUser User user) {
    return orderService.createOrderFromShopCart(user.getId(), orderRequest);
  }

  @Operation(summary = "Get an order by its id")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Order found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "User is not allowed to perform this action"),
    @ApiResponse(responseCode = "404", description = "Order not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/{id}")
  public Order findOrderById(@PathVariable(name = "id", required = true) String id) {
    return orderService.findById(id);
  }

  @Operation(summary = "Get an order of the current user and status")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Order found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "User is not allowed to perform this action"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/me")
  public Slice<Order> getOrdersOfCurrentUser(
      @RequestParam(name = "status", required = true, defaultValue = OrderStatus.Fields.DELIVERED)
          OrderStatus status,
      @CurrentUser User user,
      @PageableDefault(page = 0, size = 8, sort = "createdAt", direction = Direction.DESC)
          Pageable pageable) {
    return orderService.findOrdersByUserIdAndStatus(user.getId(), status, pageable);
  }

  @Operation(summary = "Get an order of the current user and status")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Order found"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "User is not allowed to perform this action"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/shops/{shopId}")
  public Slice<Order> getOrdersOfShop(
      @PathVariable(name = "shopId", required = true) String shopId,
      @RequestParam(name = "status", required = true, defaultValue = OrderStatus.Fields.DELIVERED)
          OrderStatus status,
      @PageableDefault(page = 0, size = 8, sort = "createdAt", direction = Direction.DESC)
          Pageable pageable) {
    return orderService.findOrdersByShopIdAndStatus(shopId, status, pageable);
  }
}
