package com.foodey.server.voucher;

import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.service.ShopService;
import com.foodey.server.shopcart.ShopCartDetail;
import com.foodey.server.shopcart.ShopCartService;
import com.foodey.server.user.model.User;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
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
    try {
      return voucherRepository.insert(voucher);
    } catch (DuplicateKeyException e) {
      throw new ResourceAlreadyInUseException("Voucher", "code", voucher.getCode());
    }
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
        Instant.now().plus(Duration.ofMinutes(2)), pageable);
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
        Instant.now().plus(Duration.ofMinutes(2)),
        pageable);
  }

  @Override
  public Voucher applyVoucherForShopCart(String voucherId, String userId, String shopId) {

    ShopCartDetail shopCartDetail = shopCartService.getDetail(shopId);

    Voucher voucher = findVoucherById(voucherId);

    if (!voucher.isActivated()) {
      throw new VoucherInvalidException("Voucher is not activated");
    } else if (voucher.isExpired()) {
      throw new VoucherInvalidException("Voucher is expired");
    } else if (!voucher.isEnoughQuantity()) {
      throw new VoucherInvalidException("Voucher is out of quantity");
    } else if (!voucher.isEnoughMiniumBuyingQuantity(shopCartDetail.getItems())) {
      throw new VoucherInvalidException("Not enough constraint products in cart");
    }

    shopCartDetail.getShopCart().setVoucher(voucher);
    shopCartService.save(shopCartDetail.getShopCart());
    return voucher;
  }

  @Override
  public Slice<Voucher> findVouchersOfCurrentShop(User user, String shopId, Pageable pageable) {
    Shop shop = shopService.findByIdAndVerifyOwner(shopId, user);
    return voucherRepository.findByShopVsBrandIdIn(
        Arrays.asList(shopId, shop.getBrandId()), pageable);
  }

  @Override
  public Voucher saveVoucher(Voucher voucher) {
    return voucherRepository.save(voucher);
  }
}
