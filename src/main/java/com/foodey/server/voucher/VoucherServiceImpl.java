package com.foodey.server.voucher;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/** VoucherServiceImpl */
@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

  private final VoucherRepository voucherRepository;

  @Override
  public Voucher createVoucher(Voucher voucher) {
    return voucherRepository.insert(voucher);
  }

  @Override
  public Voucher findVoucherById(String voucherId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findVoucherById'");
  }

  @Override
  public Slice<Voucher> findActiveVouchers(org.springframework.data.domain.Pageable pageable) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findActiveVouchers'");
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
