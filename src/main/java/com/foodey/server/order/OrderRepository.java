package com.foodey.server.order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

  Slice<Order> findByUserId(String userId, Pageable pageable);

  Slice<Order> findByShopId(String shopId, Pageable pageable);

  Slice<Order> findByShipperId(String shipperId, Pageable pageable);

  Slice<Order> findByUserIdAndStatus(String userId, OrderStatus status, Pageable pageable);

  Slice<Order> findByShopIdAndStatus(String shopId, OrderStatus status, Pageable pageable);
}
