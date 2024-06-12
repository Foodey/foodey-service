package com.foodey.server.voucher;

import com.foodey.server.user.enums.RoleType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@RequestMapping("/api/v1/vouchers")
@AllArgsConstructor
public class VoucherController {

  private final VoucherService voucherService;

  /**
   * Add voucher to the system
   *
   * @param voucher voucher object
   * @return created voucher
   */
  @Operation(summary = "Add voucher to the system")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Voucher created"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Not found"),
      })
  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  @RolesAllowed({RoleType.Fields.ADMIN, RoleType.Fields.SELLER})
  public Voucher addVoucher(@RequestBody @Valid Voucher voucher) {
    return voucherService.createVoucher(voucher);
  }

  @Operation(summary = "Get all vouchers")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Vouchers found"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Not found"),
      })
  @GetMapping("")
  @RolesAllowed({RoleType.Fields.ADMIN, RoleType.Fields.SELLER})
  public Slice<Voucher> findVouchers(
      @RequestParam(name = "active", defaultValue = "true") boolean active,
      @PageableDefault(page = 0, size = 12, direction = Direction.ASC) Pageable pageable) {
    if (active) {
      return voucherService.findActiveVouchers(pageable);
    }
    return voucherService.findAllVouchers(pageable);
  }

  @Operation(summary = "Find vouchers can be applied to shop cart")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Vouchers found"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Not found"),
      })
  @GetMapping("/shops/{shopId}")
  @RolesAllowed(RoleType.Fields.CUSTOMER)
  public Slice<Voucher> findVouchersCanBeAppliedToShopCart(
      @PathVariable("shopId") String shopId,
      @PageableDefault(page = 0, size = 12, direction = Direction.ASC) Pageable pageable) {
    return voucherService.findVouchersCanBeAppliedForShop(shopId, pageable);
  }
}
