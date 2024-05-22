package com.foodey.server.shop.service.impl;

import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.model.ShopMenu;
import com.foodey.server.shop.service.ShopMenuService;
import com.foodey.server.shop.service.ShopService;
import com.foodey.server.user.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopMenuServiceImpl implements ShopMenuService {

  private final ShopService shopService;

  @Override
  public ShopMenu createShopMenu(ShopMenu shopMenu, String shopId, User user) {
    Shop shop = shopService.findByIdAndVerifyOwner(shopId, user);

    if (shop.getMenus().stream().anyMatch(menu -> menu.getName().equals(shopMenu.getName()))) {
      throw new ResourceAlreadyInUseException("Shop.menus[].name", "name", shopMenu.getName());
    }

    shop.getMenus().add(shopMenu);
    shopService.save(shop);

    return shopMenu;
  }

  @Override
  public ShopMenu findMenuInShop(String id, String shopId) {
    Shop shop = shopService.findById(shopId);

    return shop.getMenus().stream()
        .parallel()
        .filter(menu -> menu.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException("Shop.menus[].id", "id", id));
  }

  @Override
  public boolean existsByIdAndShopId(String id, String shopId) {
    Shop shop = shopService.findById(shopId);
    return shop.getMenus().stream().anyMatch(menu -> menu.getId().equals(id));
  }

  @Override
  public boolean existsByIdInShop(String id, Shop shop) {
    return shop.getMenus().stream().anyMatch(menu -> menu.getId().equals(id));
  }

  @Override
  public List<ShopMenu> findAllInShop(String shopId) {
    Shop shop = shopService.findById(shopId);
    return shop.getMenus();
  }
}
