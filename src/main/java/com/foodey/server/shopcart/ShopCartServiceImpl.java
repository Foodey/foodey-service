package com.foodey.server.shopcart;

import com.foodey.server.order.OrderItem;
import com.foodey.server.product.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShopCartServiceImpl implements ShopCartService {

  private final ShopCartRepository shopCartRepository;
  private final ProductRepository productRepository;

  @Override
  public Optional<ShopCart> find(String userId, String shopId) {
    return shopCartRepository.findById(ShopCart.id(userId, shopId));
  }

  @Override
  public ShopCart findOrCreate(String userId, String shopId) {
    return find(userId, shopId).orElseGet(() -> new ShopCart(userId, shopId));
  }

  @Override
  public ShopCart save(ShopCart shopCart) {
    return shopCartRepository.save(shopCart);
  }

  @Override
  public void delete(String userId, String shopId) {
    shopCartRepository.deleteById(ShopCart.id(userId, shopId));
  }

  @Override
  public ShopCartDetail getDetail(String userId, String shopId) {
    ShopCart cart = shopCartRepository.findById(ShopCart.id(userId, shopId)).orElse(null);

    if (cart == null) return new ShopCartDetail(new ShopCart(userId, shopId), new ArrayList<>());

    Map<String, Long> productQuantityMap = cart.getProductsWithQuantity();

    List<OrderItem> items =
        productRepository.findAllById(productQuantityMap.keySet().stream().toList()).stream()
            .map(
                (product) -> {
                  String productId = product.getId();
                  Long quantity = productQuantityMap.get(productId);
                  double price = product.getPrice();
                  return new OrderItem(
                      productId,
                      product.getName(),
                      product.getImage(),
                      product.getDescription(),
                      product.getCategoryId(),
                      price,
                      quantity,
                      price * quantity);
                })
            .toList();
    return new ShopCartDetail(cart, items);
  }

  private boolean addProduct(String userId, String shopId, String productId, long quantity) {
    ShopCart cart = findOrCreate(userId, shopId);
    cart.addProduct(productId, quantity);
    // Map<String, Long> productQuantityMap = cart.getProductsWithQuantity();
    // productQuantityMap.put(productId, quantity + productQuantityMap.getOrDefault(productId,
    // -1L));
    return shopCartRepository.save(cart) != null;
  }

  private boolean decreaseProductQuantity(
      String userId, String shopId, String productId, long quantity) {
    ShopCart cart = findOrCreate(userId, shopId);
    cart.removeProduct(productId, quantity);
    return shopCartRepository.save(cart) != null;

    // Map<String, Long> productQuantityMap = cart.getProductsWithQuantity();
    // if (productQuantityMap.containsKey(productId)) {
    //   long newQuantity = productQuantityMap.get(productId) - quantity;
    //   if (newQuantity < 1) {
    //     productQuantityMap.remove(productId);
    //   } else {
    //     productQuantityMap.put(productId, newQuantity);
    //   }
    //   return shopCartRepository.save(cart) != null;
    // }
    // throw new ResourceNotFoundException("Product", "id", productId);
  }

  private boolean replaceProductQuantity(
      String userId, String shopId, String productId, long quantity) {
    ShopCart cart = findOrCreate(userId, shopId);
    cart.setProductQuantity(productId, quantity);
    // Map<String, Long> productQuantityMap = cart.getProductsWithQuantity();
    // productQuantityMap.put(productId, quantity);
    return shopCartRepository.save(cart) != null;
  }

  @Override
  public boolean removeProduct(String userId, String shopId, String productId) {
    ShopCart cart = findOrCreate(userId, shopId);
    cart.removeProduct(productId);
    // Map<String, Long> productQuantityMap = cart.getProductsWithQuantity();
    // productQuantityMap.remove(productId);
    return shopCartRepository.save(cart) != null;
  }

  @Override
  public void adjustProduct(
      String userId, String shopId, String productId, long quantity, ShopCartProductAction action) {
    switch (action) {
      case ADD_PRODUCT:
      case INCREASE_PRODUCT_QUANTITY:
        addProduct(userId, shopId, productId, quantity);
        break;
      case REMOVE_PRODUCT:
        removeProduct(userId, shopId, productId);
        break;
      case DECREASE_PRODUCT_QUANTITY:
        decreaseProductQuantity(userId, shopId, productId, quantity);
        break;
      case REPLACE_PRODUCT_QUANTITY:
        replaceProductQuantity(userId, shopId, productId, quantity);
        break;
      default:
        throw new UnsupportedOperationException("Unimplemented action '" + action + "'");
    }
  }

  @Override
  public void clear(String userId, String shopId) {
    shopCartRepository.deleteById(ShopCart.id(userId, shopId));
  }
}
