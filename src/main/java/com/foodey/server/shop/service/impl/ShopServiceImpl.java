package com.foodey.server.shop.service.impl;

import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.shop.model.ShopBranch;
import com.foodey.server.shop.repository.ShopBranchRepository;
import com.foodey.server.shop.repository.ShopMenuRepository;
import com.foodey.server.shop.repository.ShopRepository;
import com.foodey.server.shop.service.ShopService;
import com.foodey.server.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

  private final ShopRepository shopRepository;
  private final ShopMenuRepository shopMenuRepository;
  private final ShopBranchRepository shopBranchRepository;

  @Override
  public ShopBranch createShopBranch(ShopBranch shopBranch, User user) {
    if (shopBranchRepository.existsByNameAndOwnerId(shopBranch.getId(), user.getId())) {
      throw new ResourceAlreadyInUseException("ShopBranch", "name", shopBranch.getName());
    }

    return shopBranchRepository.save(shopBranch);
  }
}
