package com.foodey.server.shop.service;

import com.foodey.server.shop.model.ShopBranch;
import com.foodey.server.user.model.User;

public interface ShopService {

  ShopBranch createShopBranch(ShopBranch shopBranch, User user);
}
