package com.foodey.server.voucher;

import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends MongoRepository<Voucher, String> {

  boolean existsByCode(String code);

  @Query(
      value =
          "{ 'activationDate': { $lt: new Date() }, 'expiryDate': { $gt: new Date() }, $or: [ {"
              + " 'shopVsBrandId': null }, { 'shopVsBrandId': { $in: ?0 } } ] }")
  Slice<Voucher> findActiveVouchersForShop(List<String> shopVsBrandIds, Pageable pageable);

  @Query(
      value =
          "{ 'activationDate': { $lt: new Date() }, 'expiryDate': { $gt: ?1 }, $or: [ {"
              + " 'shopVsBrandId': null }, { 'shopVsBrandId': { $in: ?0 } } ] }")
  Slice<Voucher> findActiveVouchersForShopByExpiryAfter(
      List<String> shopVsBrandIds, Instant expiryDate, Pageable pageable);

  @Query(value = "{ 'activationDate': { $lt: new Date() }, 'expiryDate': { $gt: new Date() } }")
  Slice<Voucher> findActiveVouchers(Pageable pageable);

  @Query(value = "{ 'activationDate': { $lt: ?0 }, 'expiryDate': { $gt: ?1 } }")
  Slice<Voucher> findVouchersWithinTimeRange(
      Instant activationDate, Instant expiryDate, Pageable pageable);

  @Query(value = "{ 'activationDate': { $lt: new Date() }, 'expiryDate': { $gt: ?0 } }")
  Slice<Voucher> findActiveVouchersByExpiryAfter(Instant expiryDate, Pageable pageable);

  Slice<Voucher> findByShopVsBrandIdIn(List<String> shopVsBrandIds, Pageable pageable);
}
