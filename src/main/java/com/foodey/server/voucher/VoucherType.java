package com.foodey.server.voucher;

import io.swagger.v3.oas.annotations.media.Schema;

public enum VoucherType {
  @Schema(description = "Voucher type for discount on product")
  PRODUCT,

  @Schema(description = "Voucher type for discount on delivery")
  DELIVERY,

  @Schema(description = "Voucher type for discount on money")
  MONEY,
}
