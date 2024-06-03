package com.foodey.server.shop.event.listener;

import com.foodey.server.product.model.Product;
import com.foodey.server.shop.event.ProductsAddedToMenuEvent;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.service.ShopService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Slf4j
@Component
public class ProductsAddedToMenuEventListener {

  private final ShopService shopService;

  @Async
  @TransactionalEventListener
  public void handleUpdateCategoryIdsForShop(ProductsAddedToMenuEvent event) {

    List<String> categoryIds =
        event.getProducts().stream().map(Product::getCategoryId).collect(Collectors.toList());

    if (event.isAppliedToAllShop()) {
      List<Shop> shops = shopService.findByBrandId(event.getBrandId());
      shops.forEach(shop -> shop.getCategoryIds().addAll(categoryIds));
      shopService.saveAll(shops);
      log.debug("Updating categoryIds for all shops of brandId: {}", event.getBrandId());

    } else {
      Shop shop = shopService.findById(event.getShopId());
      shop.getCategoryIds().addAll(categoryIds);
      shopService.save(shop);
      log.debug("Updating categoryIds for shopId: {}", event.getShopId());
    }
  }
}
