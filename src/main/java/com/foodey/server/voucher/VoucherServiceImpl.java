package com.foodey.server.voucher;

import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.service.ShopService;
import com.foodey.server.shopcart.ShopCartService;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

  private final VoucherRepository voucherRepository;
  private final ShopCartService shopCartService;
  private final ShopService shopService;

  @Override
  public Voucher createVoucher(Voucher voucher) {
    return voucherRepository.insert(voucher);
  }

  @Override
  public Voucher findVoucherById(String voucherId) {
    return voucherRepository
        .findById(voucherId)
        .orElseThrow(() -> new ResourceNotFoundException("Voucher", "id", voucherId));
  }

  @Override
  public Slice<Voucher> findActiveVouchers(Pageable pageable) {
    return voucherRepository.findActiveVouchersByExpiryAfter(
        Instant.now().plus(Duration.ofMinutes(3)), pageable);
  }

  @Override
  public Slice<Voucher> findAllVouchers(Pageable pageable) {
    return voucherRepository.findAll(pageable);
  }

  @Override
  public Slice<Voucher> findVouchersCanBeAppliedForShop(String shopId, Pageable pageable) {
    Shop shop = shopService.findById(shopId);

    return voucherRepository.findActiveVouchersForShopByExpiryAfter(
        Arrays.asList(shopId, shop.getBrandId()),
        Instant.now().plus(Duration.ofMinutes(3)),
        pageable);
  }

  @Override
  public void applyVoucherForShopCart(String userId, String shopId) {}
}
