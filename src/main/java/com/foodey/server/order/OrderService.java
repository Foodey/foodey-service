package com.foodey.server.order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface OrderService {

  Order createOrderFromShopCart(String userId, OrderRequest orderRequest);

  Order findById(String orderId);

  Slice<Order> findByUserId(String userId, Pageable pageable);

  Slice<Order> findByShopId(String restaurantId, Pageable pageable);

  Order updateOrderStatus(String orderId, OrderStatus status);

  Order save(Order order);

  void deleteById(String orderId);

  Slice<Order> findOrdersByUserIdAndStatus(String userId, OrderStatus status, Pageable pageable);

  Slice<Order> findOrdersByShopIdAndStatus(String shopId, OrderStatus status, Pageable pageable);
}
