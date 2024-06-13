package com.foodey.server.voucher;

import com.foodey.server.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface VoucherService {

  Voucher saveVoucher(Voucher voucher);

  Voucher createVoucher(Voucher voucher);

  Voucher findVoucherById(String voucherId);

  Slice<Voucher> findActiveVouchers(Pageable pageable);

  Slice<Voucher> findAllVouchers(Pageable pageable);

  Slice<Voucher> findVouchersCanBeAppliedForShop(String shopId, Pageable pageable);

  Slice<Voucher> findVouchersOfCurrentShop(User user, String shopId, Pageable pageable);

  Voucher applyVoucherForShopCart(String voucherId, String userId, String shopId);
}
