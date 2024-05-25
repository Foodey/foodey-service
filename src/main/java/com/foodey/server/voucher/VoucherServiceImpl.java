package com.foodey.server.voucher;

import com.foodey.server.order.OrderItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/** VoucherServiceImpl */
@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

  private final VoucherRepository voucherRepository;

  @Override
  public Voucher createVoucher(Voucher voucher) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createVoucher'");
  }

  @Override
  public Voucher findById(String voucherId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findById'");
  }

  @Override
  public Slice<Voucher> getActiveVouchers(Pageable pageable) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getActiveVouchers'");
  }

  @Override
  public Voucher findVoucherByCodeAndCanBeUsed(
      String code, List<OrderItem> boughtOrderItems, String storeId, String customerId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findVoucherByCodeAndCanBeUsed'");
  }

  // @Override
  // @Transactional
  // public Voucher createVoucher(Voucher voucher) {

  //   voucher.generateCode((code) -> voucherRepository.existsByCodeAndActive(code, Instant.now()));
  //   return voucherRepository.save(voucher);
  // }

  // @Override
  // public Voucher getActiveVoucher(String code) {
  //   return voucherRepository
  //       .findByCodeAndActive(code)
  //       .orElseThrow(() -> new NotFoundException("Voucher not found"));
  // }

  // @Override
  // public List<Voucher> getActiveVouchers(int page, int limit) {
  //   return voucherRepository.findByActive((page - 1) * limit, limit).orElse(new ArrayList<>());
  // }

  // @Override
  // public Voucher findVoucherByCodeAndCanBeUsed(
  //     String code, List<OrderItem> boughtOrderItems, String storeId, String customerId) {
  //   Voucher voucher =
  //       voucherRepository
  //           .findByCodeAndActive(code)
  //           .orElseThrow(() -> new NotFoundException("Voucher not found with code: " + code));
  //   if (!voucher.canBeUsed(boughtOrderItems, storeId, customerId)) {
  //     throw new NotFoundException("Voucher cannot be applied to this order");
  //   }
  //   return voucher;
  // }
}
