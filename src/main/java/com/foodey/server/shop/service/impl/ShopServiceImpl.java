package com.foodey.server.shop.service.impl;

import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.repository.ShopRepository;
import com.foodey.server.shop.service.ShopBranchService;
import com.foodey.server.shop.service.ShopService;
import com.foodey.server.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

  private final ShopRepository shopRepository;
  private final ShopBranchService shopBranchService;

  @Override
  public Shop createShop(Shop shop, User user) {
    String userId = user.getId();
    String branchId = shop.getBranchId();

    if (!shopBranchService.existsByIdAndOwnerId(branchId, userId))
      throw new ResourceNotFoundException("ShopBranch", "id", branchId);
    else if (shopRepository.existsByNameAndBranchId(shop.getName(), branchId))
      throw new ResourceAlreadyInUseException("Shop", "name", shop.getName());

    shop.setOwnerId(user.getId());
    shop.setBranchId(branchId);

    return shopRepository.save(shop);
  }

  @Override
  public Shop findById(String id) {
    return shopRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Shop", "id", id));
  }

  @Override
  public Page<Shop> findAll(Pageable pageable) {
    return shopRepository.findAll(pageable);
  }

  @Override
  public Shop save(Shop shop) {
    return shopRepository.save(shop);
  }

  @Override
  public Shop findByIdAndVerifyOwner(String id, User user) {
    Shop shop = findById(id);

    if (!shop.getOwnerId().equals(user.getId()))
      throw new AccessDeniedException("You are not owner of this shop.");

    return shop;
  }
}
