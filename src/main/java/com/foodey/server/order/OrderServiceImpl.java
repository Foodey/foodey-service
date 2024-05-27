package com.foodey.server.order;

import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.payment.Payment;
import com.foodey.server.payment.PaymentStatus;
import com.foodey.server.shop.service.ShopService;
import com.foodey.server.shopcart.ShopCartDetail;
import com.foodey.server.shopcart.ShopCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final ShopCartService shopCartService;
  private final ShopService shopService;

  @Override
  @Transactional
  public Order createOrderFromShopCart(String userId, OrderRequest orderRequest) {
    String shopId = orderRequest.getShopId();
    ShopCartDetail shopCart = shopCartService.getDetail(userId, shopId);

    Payment payment =
        new Payment(
            orderRequest.getPaymentMethod(), PaymentStatus.PENDING, shopCart.getTotalPrice());

    Order order =
        new Order(
            userId,
            shopService.findById(shopId),
            null,
            orderRequest.getAddress(),
            payment,
            orderRequest.getVoucherCode(),
            orderRequest.getNote(),
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
  @Transactional
  public Order updateOrderStatus(String orderId, OrderStatus status) {
    Order order = findById(orderId);
    order.setStatus(status);
    return orderRepository.save(order);
  }

  @Override
  @Transactional
  public Order save(Order order) {
    return orderRepository.save(order);
  }

  @Override
  @Transactional
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
