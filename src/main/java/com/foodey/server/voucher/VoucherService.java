package com.foodey.server.voucher;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface VoucherService {

  Voucher createVoucher(Voucher voucher);

  Voucher findVoucherById(String voucherId);

  Slice<Voucher> findActiveVouchers(Pageable pageable);

  // Voucher findVoucherByCodeAndCanBeUsed(
  //     String code, List<OrderItem> boughtOrderItems, String storeId, String customerId);

  // Slice<Voucher> findVoucherCanBeAppliedToShopCart();
}
