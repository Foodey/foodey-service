package com.foodey.server.order;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.payment.Payment;
import com.foodey.server.payment.PaymentStatus;
import com.foodey.server.shopcart.ShopCartDetail;
import com.foodey.server.shopcart.ShopCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/** OrderServiceImpl */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final ShopCartService shopCartService;

  @Override
  public Order createOrderFromShopCart(String userId, OrderRequest orderRequest) {
    ShopCartDetail shopCart = shopCartService.getDetail(userId, orderRequest.getShopId());

    Payment payment =
        new Payment(
            orderRequest.getPaymentMethod(), PaymentStatus.PENDING, shopCart.getTotalPrice());

    Order order =
        new Order(
            userId,
            orderRequest.getShopId(),
            NanoIdUtils.randomNanoId(),
            orderRequest.getAddress(),
            payment,
            orderRequest.getVoucherCode(),
            shopCart.getItems());

    return orderRepository.save(order);
  }

  @Override
  public Order findById(String orderId) {
    return orderRepository
        .findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
  }

  @Override
  public Slice<Order> findByUserId(String userId, Pageable pageable) {
    return orderRepository.findByUserId(userId, pageable);
  }

  @Override
  public Slice<Order> findByShopId(String shopId, Pageable pageable) {
    return orderRepository.findByShopId(shopId, pageable);
  }

  @Override
  public Order updateOrderStatus(String orderId, OrderStatus status) {
    Order order = findById(orderId);
    order.setStatus(status);
    return orderRepository.save(order);
  }

  @Override
  public Order save(Order order) {
    return orderRepository.save(order);
  }

  @Override
  public void deleteById(String orderId) {
    orderRepository.deleteById(orderId);
  }

  @Override
  public Slice<Order> findOrdersByUserIdAndStatus(
      String userId, OrderStatus status, Pageable pageable) {
    return orderRepository.findByUserIdAndStatus(userId, status, pageable);
  }

  @Override
  public Slice<Order> findOrdersByShopIdAndStatus(
      String shopId, OrderStatus status, Pageable pageable) {
    return orderRepository.findByShopIdAndStatus(shopId, status, pageable);
  }
}
