package com.foodey.server.shop.service;

import com.foodey.server.shop.model.ShopBranch;
import com.foodey.server.user.model.User;
import java.util.List;

public interface ShopBranchService {

  ShopBranch createShopBranch(ShopBranch shopBranch, User user);

  ShopBranch findById(String id);

  ShopBranch findByIdAndVerifyOwner(String id, User user);

  List<ShopBranch> findByOwnerId(String ownerId);

  boolean existsByIdAndOwnerId(String id, String ownerId);
}
