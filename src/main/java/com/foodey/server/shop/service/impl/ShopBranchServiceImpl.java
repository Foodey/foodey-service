package com.foodey.server.shop.service.impl;

import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.shop.model.ShopBranch;
import com.foodey.server.shop.repository.ShopBranchRepository;
import com.foodey.server.shop.service.ShopBranchService;
import com.foodey.server.user.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopBranchServiceImpl implements ShopBranchService {

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

  @Override
  public boolean existsByIdAndOwnerId(String id, String ownerId) {
    return shopBranchRepository.existsByIdAndOwnerId(id, ownerId);
  }

  @Override
  public ShopBranch findByIdAndVerifyOwner(String id, User user) {
    ShopBranch shopBranch = findById(id);
    if (!shopBranch.getOwnerId().equals(user.getId())) {
      throw new AccessDeniedException("User does not own this shop branch");
    }
    return shopBranch;
  }

  @Override
  public ShopBranch save(ShopBranch shopBranch) {
    return shopBranchRepository.save(shopBranch);
  }

  @Override
  public Slice<ShopBranch> findAll(Pageable pageable) {
    return shopBranchRepository.findAll(pageable);
  }
}
