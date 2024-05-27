package com.foodey.server.voucher;

import io.swagger.v3.oas.annotations.media.Schema;

public enum VoucherMethod {
  @Schema(description = "The voucher is a percentage discount")
  PERCENTAGE,

  @Schema(description = "The voucher is a fixed amount discount")
  SPECIAL_AMOUNT,
}
