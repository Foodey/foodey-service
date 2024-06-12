package com.foodey.server.voucher;

import com.foodey.server.user.enums.RoleType;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vouchers")
@AllArgsConstructor
public class VoucherController {

  private final VoucherService voucherService;

  // @GetMapping("")
  // public ResponseEntity<?> getVouchers(
  //     @RequestParam(defaultValue = "1") int page,
  //     @RequestParam(defaultValue = "false") boolean all,
  //     @RequestParam(defaultValue = "5") int limit) {
  //   return all
  //       ? ResponseEntity.ok(voucherService.getActiveVouchers(1, limit * page))
  //       : ResponseEntity.ok(voucherService.getActiveVouchers(page, limit));
  // }

  /**
   * Add voucher to the system
   *
   * @param voucher voucher object
   * @return created voucher
   */
  @PostMapping("")
  @RolesAllowed({RoleType.Fields.ADMIN, RoleType.Fields.SELLER})
  @ResponseStatus(HttpStatus.CREATED)
  public Voucher addVoucher(@RequestBody @Valid Voucher voucher) {
    return voucherService.createVoucher(voucher);
  }

  // /**
  //  * Get voucher by code
  //  *
  //  * @param code voucher code
  //  * @return found voucher
  //  */
  // @GetMapping("/{code}")
  // public ResponseEntity<?> getVoucher(@PathVariable(name = "code") String code) {
  //   return ResponseEntity.ok(voucherService.getActiveVoucher(code));
  // }
}
