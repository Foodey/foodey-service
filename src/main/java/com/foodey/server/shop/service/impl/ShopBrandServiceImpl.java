package com.foodey.server.shop.service.impl;

import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.shop.model.ShopBrand;
import com.foodey.server.shop.repository.ShopBrandRepository;
import com.foodey.server.shop.service.ShopBrandService;
import com.foodey.server.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopBrandServiceImpl implements ShopBrandService {

  private final ShopBrandRepository shopBrandRepository;

  // Create

  @Override
  public ShopBrand createShopBrand(ShopBrand shopBrand, User user) {
    if (shopBrandRepository.existsByNameAndOwnerId(shopBrand.getName(), user.getId())) {
      throw new ResourceAlreadyInUseException("ShopBrand", "name", shopBrand.getName());
    }

    shopBrand.setOwnerId(user.getId());

    return shopBrandRepository.save(shopBrand);
  }

  // Find

  @Override
  public ShopBrand findById(String id) {
    return shopBrandRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("ShopBrand", "id", id));
  }

  @Override
  public void verifyOwner(String brachId, String userId) {
    if (!shopBrandRepository.existsByIdAndOwnerId(brachId, userId)) {
      throw new AccessDeniedException("You are not owner of this brand.");
    }
  }

  @Override
  public ShopBrand findByIdAndVerifyOwner(String id, String userId) {
    ShopBrand shopBrand = findById(id);
    if (!shopBrand.getOwnerId().equals(userId)) {
      throw new AccessDeniedException("You are not owner of this brand.");
    }
    return shopBrand;
  }

  @Override
  public Slice<ShopBrand> findByOwnerId(String ownerId, Pageable pageable) {
    return shopBrandRepository.findByOwnerId(ownerId, pageable);
  }

  @Override
  public boolean existsByIdAndOwnerId(String id, String ownerId) {
    return shopBrandRepository.existsByIdAndOwnerId(id, ownerId);
  }

  @Override
  public Slice<ShopBrand> findAll(Pageable pageable) {
    return shopBrandRepository.findAll(pageable);
  }

  // update
  @Override
  public ShopBrand save(ShopBrand shopBrand) {
    return shopBrandRepository.save(shopBrand);
  }
}
