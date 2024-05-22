package com.foodey.server.product.service.impl;

import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.product.model.Product;
import com.foodey.server.product.repository.ProductCategoryRepository;
import com.foodey.server.product.repository.ProductRepository;
import com.foodey.server.product.service.ProductService;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.model.ShopMenu;
import com.foodey.server.shop.service.ShopMenuService;
import com.foodey.server.shop.service.ShopService;
import com.foodey.server.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ShopMenuService shopMenuService;
  private final ShopService shopService;
  private final ProductCategoryRepository productCategoryRepository;

  @Override
  public Product createProduct(Product product, User user) {

    String categoryId = product.getCategoryId();
    if (!productCategoryRepository.existsById(categoryId))
      throw new ResourceNotFoundException("ProductCategory", "id", categoryId);

    Shop shop = shopService.findByIdAndVerifyOwner(product.getShopId(), user);

    ShopMenu shopMenu =
        shop.getMenus().stream()
            .filter(menu -> menu.getId().equals(product.getMenuId()))
            .findFirst()
            .orElseThrow(
                () -> new ResourceNotFoundException("ShopMenu", "id", product.getMenuId()));

    Product createdProduct = productRepository.save(product);
    shopMenu.getProductIds().add(createdProduct.getId());

    shop.getCategories().add(categoryId);
    shopService.save(shop);

    return createdProduct;
  }

  @Override
  public Product findById(String id) {
    return productRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
  }

  @Override
  public Page<Product> findAll(Pageable pageable) {
    return productRepository.findAll(pageable);
  }

  @Override
  public Product save(Product product) {
    return productRepository.save(product);
  }

  @Override
  public Page<Product> findByShopIdAndMenuId(String shopId, String menuId, Pageable pageable) {

    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findByShopIdAndMenuId'");
  }
}
