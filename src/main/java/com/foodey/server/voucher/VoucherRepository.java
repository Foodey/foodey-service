package com.foodey.server.voucher;

import org.springframework.data.mongodb.repository.MongoRepository;

/** VoucherRepository */
public interface VoucherRepository extends MongoRepository<Voucher, String> {

  // boolean existsByCode(String code);

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

  // @Aggregation(
  //     pipeline = {
  //       "{ $match: { $expr: { $and: [ { $lte: [ '$activationDate', ?1 ] }, { $gte: ["
  //           + " '$expiryDate', ?1 ] } ] } } }",
  //       "{ $skip: ?0 }",
  //       "{ $limit: ?1 }"
  //     })
  // Optional<List<Voucher>> findByActive(Instant now, int skip, int limit);
}
