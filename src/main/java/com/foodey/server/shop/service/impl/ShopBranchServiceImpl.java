package com.foodey.server.shop.service.impl;

import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.shop.model.ShopBranch;
import com.foodey.server.shop.repository.ShopBranchRepository;
import com.foodey.server.shop.repository.ShopMenuRepository;
import com.foodey.server.shop.repository.ShopRepository;
import com.foodey.server.shop.service.ShopBranchService;
import com.foodey.server.user.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopBranchServiceImpl implements ShopBranchService {

  private final ShopRepository shopRepository;
  private final ShopMenuRepository shopMenuRepository;
  private final ShopBranchRepository shopBranchRepository;

  @Override
  public ShopBranch createShopBranch(ShopBranch shopBranch, User user) {
    if (shopBranchRepository.existsByNameAndOwnerId(shopBranch.getName(), user.getId())) {
      throw new ResourceAlreadyInUseException("ShopBranch", "name", shopBranch.getName());
    }

    shopBranch.setOwnerId(user.getId());

    return shopBranchRepository.save(shopBranch);
  }

  @Override
  public ShopBranch findById(String id) {
    return shopBranchRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("ShopBranch", "id", id));
  }

  @Override
  public List<ShopBranch> findByOwnerId(String ownerId) {
    return shopBranchRepository.findByOwnerId(ownerId);
  }
}
