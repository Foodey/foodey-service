package com.foodey.server.shop.service.impl;

import com.foodey.server.product.model.Product;
import com.foodey.server.product.repository.ProductRepository;
import com.foodey.server.shop.dto.MenuView;
import com.foodey.server.shop.event.ProductAddedToMenuEvent;
import com.foodey.server.shop.exceptions.MenuSizeTooBigException;
import com.foodey.server.shop.model.Menu;
import com.foodey.server.shop.model.ShopBrand;
import com.foodey.server.shop.service.MenuService;
import com.foodey.server.shop.service.ShopBrandService;
import com.foodey.server.user.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

  private final ProductRepository productRepository;
  private final ShopBrandService shopBrandService;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  @Transactional
  public List<Product> addProductsToMenu(
      User user, String brandId, String shopId, List<Product> products, boolean appliedToAllShops) {
    ShopBrand shopBrand = shopBrandService.findById(brandId);
    Menu menu = shopBrand.getMenu();

    for (Product product : products) {
      if (appliedToAllShops) {
        product.setBrandId(brandId);
      } else {
        product.setShopId(shopId);
      }
    }

    List<Product> savedProducts = productRepository.saveAll(products);
    if (appliedToAllShops) {
      menu.addProducts(savedProducts);
    } else {
      menu.addProductsToShop(shopId, savedProducts);
    }
    shopBrandService.save(shopBrand);

    applicationEventPublisher.publishEvent(
        new ProductAddedToMenuEvent(this, user, brandId, shopId, savedProducts, appliedToAllShops));
    return savedProducts;
  }

  @Override
  @Transactional
  public Product addProductToMenu(
      User user, String brandId, String shopId, Product product, boolean appliedToAllShops) {
    ShopBrand shopBrand = shopBrandService.findById(brandId);
    Menu menu = shopBrand.getMenu();

    if (appliedToAllShops) {
      product.setBrandId(brandId);
    } else {
      product.setShopId(shopId);
    }

    Product savedProduct = productRepository.save(product);
    if (appliedToAllShops) {
      menu.addProduct(product);
    } else {
      menu.addProductToShop(shopId, savedProduct);
    }
    shopBrandService.save(shopBrand);

    applicationEventPublisher.publishEvent(
        new ProductAddedToMenuEvent(
            this, user, brandId, shopId, List.of(product), appliedToAllShops));

    return savedProduct;
  }

  @Override
  public void validateMenuSize(Menu menu) {
    if (menu.getNumberOfProducts() >= 150) throw new MenuSizeTooBigException(150);
  }

  @Override
  public MenuView getMenuInShop(String brandId, String shopId) {
    ShopBrand brand = shopBrandService.findById(brandId);
    Menu menu = brand.getMenu();
    List<Product> products = productRepository.findAllById(menu.getProductIdsInShop(shopId));

    return MenuView.builder()
        .categoryNames(menu.getSortedCategoryNamesInShop(shopId))
        .products(products)
        .numberOfProducts(products.size())
        .build();
  }

  @Override
  public MenuView getMenuInShop(String brandId, String shopId, String categoryName) {
    ShopBrand brand = shopBrandService.findById(brandId);
    Menu menu = brand.getMenu();

    List<Product> products =
        productRepository.findAllById(menu.getProductIdsInShop(shopId, categoryName));

    return MenuView.builder()
        .categoryNames(menu.getSortedCategoryNamesInShop(shopId))
        .products(products)
        .currentCategoryName(categoryName)
        .numberOfProducts(products.size())
        .build();
  }

  @Override
  public MenuView getMenuInBrand(String brandId) {
    ShopBrand brand = shopBrandService.findById(brandId);
    Menu menu = brand.getMenu();
    List<Product> products = productRepository.findAllById(menu.getProductIds());

    return MenuView.builder()
        .categoryNames(menu.getSortedCategoryNames())
        .products(products)
        .numberOfProducts(products.size())
        .build();
  }

  @Override
  public MenuView getMenuInBrand(String brandId, String categoryName) {
    ShopBrand brand = shopBrandService.findById(brandId);
    Menu menu = brand.getMenu();
    List<Product> products = productRepository.findAllById(menu.getProductIds(categoryName));

    return MenuView.builder()
        .categoryNames(menu.getSortedCategoryNames())
        .products(products)
        .currentCategoryName(categoryName)
        .numberOfProducts(products.size())
        .build();
  }
}
