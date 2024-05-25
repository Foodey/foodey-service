package com.foodey.server.voucher;

import com.foodey.server.order.OrderItem;
import java.util.List;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Slice;

/** VoucherService */
public interface VoucherService {

  Voucher createVoucher(Voucher voucher);

  Voucher findById(String voucherId);

  Slice<Voucher> getActiveVouchers(Pageable pageable);

  Voucher findVoucherByCodeAndCanBeUsed(
      String code, List<OrderItem> boughtOrderItems, String storeId, String customerId);
}
