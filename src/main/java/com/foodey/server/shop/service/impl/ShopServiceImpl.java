package com.foodey.server.shop.service.impl;

import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.repository.ShopRepository;
import com.foodey.server.shop.service.ShopBrandService;
import com.foodey.server.shop.service.ShopService;
import com.foodey.server.user.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

  private final ShopRepository shopRepository;
  private final ShopBrandService shopBrandService;

  @Override
  public Shop createShop(Shop shop, User user) {
    String brandId = shop.getBrandId();
    String userId = user.getId();

    shopBrandService.verifyOwner(brandId, userId);

    if (shopRepository.existsByNameAndBrandId(shop.getName(), brandId))
      throw new ResourceAlreadyInUseException("Shop", "name", shop.getName());

    shop.setOwnerId(userId);
    shop.setBrandId(brandId);

    return shopRepository.save(shop);
  }

  @Override
  public Shop findById(String id) {
    return shopRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Shop", "id", id));
  }

  @Override
  public Slice<Shop> findAll(Pageable pageable) {
    return shopRepository.findAll(pageable);
  }

  @Override
  public Shop save(Shop shop) {
    return shopRepository.save(shop);
  }

  @Override
  public List<Shop> saveAll(List<Shop> shops) {
    return shopRepository.saveAll(shops);
  }

  @Override
  public Slice<Shop> findByCategoryId(String category, Pageable pageable) {
    return shopRepository.findByCategoryIdsContaining(category, pageable);
  }

  @Override
  public Shop findByIdAndVerifyOwner(String id, String userId) {
    Shop shop = findById(id);
    if (!shop.getOwnerId().equals(userId))
      throw new AccessDeniedException("You are not owner of this shop.");
    return shop;
  }

  @Override
  public List<Shop> findByBrandId(String brandId) {
    return shopRepository.findByBrandId(brandId);
  }

  @Override
  public Slice<Shop> findByBrandId(String brandId, Pageable pageable) {
    return shopRepository.findByBrandId(brandId, pageable);
  }

  @Override
  public Shop findByIdAndBrandIdAndVerifyOwner(String id, String brandId, String userId) {
    Shop shop =
        shopRepository
            .findByIdAndBrandId(id, brandId)
            .orElseThrow(() -> new ResourceNotFoundException("Shop", "id", id));

    if (!shop.getOwnerId().equals(userId))
      throw new AccessDeniedException("You are not owner of this shop.");
    return shop;
  }
}
