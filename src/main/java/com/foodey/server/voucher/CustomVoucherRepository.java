package com.foodey.server.voucher;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/** CustomVoucherRepository */
public interface CustomVoucherRepository {

  Slice<Voucher> findActiveVouchers(Pageable pageable);
}
