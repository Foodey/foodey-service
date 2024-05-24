package com.foodey.server.shop.service;

import com.foodey.server.shop.model.ShopBranch;
import com.foodey.server.user.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ShopBranchService {

  ShopBranch createShopBranch(ShopBranch shopBranch, User user);

  ShopBranch findById(String id);

  ShopBranch findByIdAndVerifyOwner(String id, User user);

  List<ShopBranch> findByOwnerId(String ownerId);

  boolean existsByIdAndOwnerId(String id, String ownerId);

  ShopBranch save(ShopBranch shopBranch);

  Slice<ShopBranch> findAll(Pageable pageable);
}
