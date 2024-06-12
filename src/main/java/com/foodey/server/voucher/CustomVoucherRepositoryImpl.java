package com.foodey.server.voucher;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CustomVoucherRepositoryImpl implements CustomVoucherRepository {

  private MongoTemplate mongoTemplate;

  @Override
  public Slice<Voucher> findActiveVouchers(Pageable pageable) {
    Query query = new Query();

    query.addCriteria(
        Criteria.where("activationDate")
            .lt(Instant.now())
            .and("expiryDate")
            .gt(Instant.now().minus(Duration.ofMinutes(3))));
    query.with(pageable);

    List<Voucher> vouchers = mongoTemplate.find(query, Voucher.class);
    boolean hasNext = vouchers.size() == pageable.getPageSize();

    return new SliceImpl<>(vouchers, pageable, hasNext);
  }
}
