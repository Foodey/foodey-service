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

  // @Query(
  //     value = "{'code': ?0, 'activationDate': { $lte:  ?1}, 'expiryDate': { $gte: ?1 } }",
  //     exists = true)
  // boolean existsByCodeAndActive(String code, Instant now);

  // @Query(
  //     value =
  //         "{ $expr: { $and: [ { $eq: [ '$code', ?0 ] }, { $lte: [ '$activationDate', new Date()"
  //             + " ] }, { $gte: [ '$expiryDate', new Date() ] } ] } }",
  //     exists = true)
  // boolean existsByCodeAndActive(String code);

  // @Aggregation(
  //     pipeline = {
  //       "{ $match: { $expr: { $and: [ { $eq: [ '$code', ?0 ] }, { $lte: [ '$activationDate',
  // $$NOW"
  //           + " ] }, { $gte: [ '$expiryDate', $$NOW ] } ] } } }",
  //       "{ $limit: 1 }"
  //     })
  // Optional<Voucher> findByCodeAndActive(String code);

  // @Aggregation(
  //     pipeline = {
  //       "{ $match: { $expr: { $and: [ { $eq: [ '$code', ?0 ] }, { $lte: [ '$activationDate', ?1"
  //           + " ] }, { $gte: [ '$expiryDate', ?1 ] } ] } } }",
  //       "{ $limit: 1 }"
  //     })
  // Optional<Voucher> findByCodeAndActive(String code, Instant now);

  // @Aggregation(
  //     pipeline = {
  //       "{ $match: { $expr: { $and: [ { $lte: [ '$activationDate', $$NOW ] }, { $gte: ["
  //           + " '$expiryDate', $$NOW ] } ] } } }",
  //       "{ $skip: ?0 }",
  //       "{ $limit: ?1 }"
  //     })
  // Optional<List<Voucher>> findByActive(int skip, int limit);
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
}
