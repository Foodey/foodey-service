package com.foodey.server.order;

import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.payment.Payment;
import com.foodey.server.payment.PaymentStatus;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.service.ShopService;
import com.foodey.server.shopcart.ShopCartDetail;
import com.foodey.server.shopcart.ShopCartService;
import com.foodey.server.user.model.User;
import com.foodey.server.voucher.Voucher;
import com.foodey.server.voucher.VoucherInvalidException;
import com.foodey.server.voucher.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final ShopCartService shopCartService;
  private final ShopService shopService;
  private final VoucherService voucherService;

  @Override
  @Transactional
  public Order createOrderFromShopCart(User user, OrderRequest orderRequest) {
    String shopId = orderRequest.getShopId();
    String userId = user.getId();

    ShopCartDetail shopCart = shopCartService.getDetail(userId, shopId);

    if (shopCart.getItems().isEmpty()) {
      throw new ResourceNotFoundException("ShopCart", "shopId:userId", shopId + ":" + userId);
    }

    Voucher voucher = shopCart.getVoucher();
    String voucherCode = null;
    String voucherName = null;

    if (voucher != null) {
      if (voucher.isExpired()) {
        throw new VoucherInvalidException("Voucher is expired");
      } else if (!voucher.isEnoughQuantity()) {
        throw new VoucherInvalidException("Voucher is out of stock");
      }

      voucherCode = voucher.getCode();
      voucherName = voucher.getName();
      voucherService.saveVoucher(voucher.apply());
    }

    Payment payment =
        new Payment(
            orderRequest.getPaymentMethod(),
            PaymentStatus.PENDING,
            shopCart.getPriceAfterDiscount());

    Shop shop = shopService.findById(shopId);

    Order order =
        new Order(
            user,
            shop,
            shop.getId(),
            shop.getName(),
            null,
            orderRequest.getShippingAddress(),
            payment,
            voucherCode,
            voucherName,
            shopCart.getTotalDiscount(),
            orderRequest.getNote(),
            shopCart.getItems());

    Order newOrder = orderRepository.save(order);

    shopCartService.clear(userId, shopId);

    return newOrder;
  }

  @Override
  public Order findById(String orderId) {
    return orderRepository
        .findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
  }

  @Override
  public Order findByIdAndUserId(String orderId, String userId) {
    return orderRepository
        .findByIdAndUserId(orderId, userId)
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

  @Override
  public Order findByIdAndVerifyOwner(String orderId, String userId) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

    if (!order.getUserId().equals(userId)) {
      throw new AccessDeniedException("You are not the owner of this order");
    }
    return order;
  }

  @Override
  public void cancelOrder(String orderId) {
    Order order = findById(orderId);

    if (order.getStatus() != OrderStatus.PENDING) {
      throw new AccessDeniedException("You can only cancel pending order");
    }

    order.setStatus(OrderStatus.CANCELED);
    orderRepository.save(order);
  }

  @Override
  public void confirmOrder(String orderId) {
    Order order = findById(orderId);

    if (order.getStatus() != OrderStatus.PENDING) {
      throw new AccessDeniedException("You can only confirm pending order");
    }

    order.setStatus(OrderStatus.STORE_CONFIRMED);
    orderRepository.save(order);
  }
}
