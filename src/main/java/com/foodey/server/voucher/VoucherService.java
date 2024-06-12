package com.foodey.server.voucher;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface VoucherService {

  Voucher createVoucher(Voucher voucher);

  Voucher findVoucherById(String voucherId);

  Slice<Voucher> findActiveVouchers(Pageable pageable);

  Slice<Voucher> findAllVouchers(Pageable pageable);

  Slice<Voucher> findVouchersCanBeAppliedForShop(String shopId, Pageable pageable);

  void applyVoucherForShopCart(String userId, String shopId);
}
