package com.foodey.server.order;

import com.foodey.server.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface OrderService {

  Order createOrderFromShopCart(String userId, OrderRequest orderRequest);

  Order findById(String orderId);

  Order findByIdAndUserId(String orderId, String userId);

  Slice<Order> findByUserId(String userId, Pageable pageable);

  Slice<Order> findByShopId(String restaurantId, Pageable pageable);

  Order findByIdAndVerifyOwner(String orderId, String userId);

  default Order findByIdAndVerifyOwner(String orderId, User user) {
    return findByIdAndVerifyOwner(orderId, user.getId());
  }

  Order updateOrderStatus(String orderId, OrderStatus status);

  Order save(Order order);

  void deleteById(String orderId);

  Slice<Order> findOrdersByUserIdAndStatus(String userId, OrderStatus status, Pageable pageable);

  Slice<Order> findOrdersByShopIdAndStatus(String shopId, OrderStatus status, Pageable pageable);
}
