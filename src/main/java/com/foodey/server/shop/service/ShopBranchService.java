package com.foodey.server.shop.service;

import com.foodey.server.shop.model.ShopBranch;
import com.foodey.server.user.model.User;
import java.util.List;

public interface ShopBranchService {

  ShopBranch createShopBranch(ShopBranch shopBranch, User user);

  ShopBranch findById(String id);

  List<ShopBranch> findByOwnerId(String ownerId);
}
