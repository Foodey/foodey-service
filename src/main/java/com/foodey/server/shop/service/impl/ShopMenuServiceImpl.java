package com.foodey.server.shop.service.impl;

import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.product.repository.ProductRepository;
import com.foodey.server.shop.model.MenuResponse;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.model.ShopBranch;
import com.foodey.server.shop.model.ShopMenu;
import com.foodey.server.shop.service.ShopBranchService;
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
  private final ProductRepository productRepository;
  private final ShopBranchService shopBranchService;

  @Override
  public ShopMenu createShopMenu(ShopMenu shopMenu, String shopId, User user) {
    Shop shop = shopService.findByIdAndVerifyOwner(shopId, user);

    shop.getMenus().add(shopMenu);

    shopService.save(shop);

    return shopMenu;
  }

  @Override
  public ShopMenu findMenuInShop(String id, String shopId) {
    Shop shop = shopService.findById(shopId);

    ShopMenu menuFound =
        shop.getMenus().stream()
            .parallel()
            .filter(menu -> menu.getId().equals(id))
            .findFirst()
            .orElseGet(
                () -> {
                  ShopBranch shopBranch = shopBranchService.findById(shop.getBranchId());
                  return shopBranch.getMenus().stream()
                      .parallel()
                      .filter(menu -> menu.getId().equals(id))
                      .findFirst()
                      .orElseThrow(() -> new ResourceNotFoundException("Menu", "id", id));
                });

    return menuFound;
  }

  @Override
  public boolean existsInShop(String id, String shopId) {
    Shop shop = shopService.findById(shopId);
    if (shop.getMenus().stream().anyMatch(menu -> menu.getId().equals(id))) {
      return true;
    } else {
      ShopBranch shopBranch = shopBranchService.findById(shop.getBranchId());
      return shopBranch.getMenus().stream().anyMatch(menu -> menu.getId().equals(id));
    }
  }

  @Override
  public boolean existsInShop(String id, Shop shop) {
    if (shop.getMenus().stream().anyMatch(menu -> menu.getId().equals(id))) {
      return true;
    } else {
      ShopBranch shopBranch = shopBranchService.findById(shop.getBranchId());
      return shopBranch.getMenus().stream().anyMatch(menu -> menu.getId().equals(id));
    }
  }

  @Override
  public List<ShopMenu> findAllInShop(String shopId) {
    Shop shop = shopService.findById(shopId);

    ShopBranch shopBranch = shopBranchService.findById(shop.getBranchId());
    shop.getMenus().addAll(shopBranch.getMenus());

    return shop.getMenus();
  }

  @Override
  public List<MenuResponse> findAllDetailsInShop(String shopId) {
    Shop shop = shopService.findById(shopId);
    ShopBranch shopBranch = shopBranchService.findById(shop.getBranchId());
    shop.getMenus().addAll(shopBranch.getMenus());

    return shop.getMenus().stream()
        .parallel()
        .map(menu -> new MenuResponse(menu, productRepository.findAllById(menu.getProductIds())))
        .toList();
  }

  @Override
  public MenuResponse findMenuDetailsInShop(String id, String shopId) {
    ShopMenu menu = findMenuInShop(id, shopId);
    return new MenuResponse(menu, productRepository.findAllById(menu.getProductIds()));
  }
}
