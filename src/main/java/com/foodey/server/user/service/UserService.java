package com.foodey.server.user.service;

import com.foodey.server.auth.dto.RegistrationRequest;
import com.foodey.server.product.model.FavoriteProduct;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.user.model.User;
import com.foodey.server.user.model.decorator.NewRoleRequest;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface UserService {

  Optional<User> findByPhoneNumber(String phoneNumber);

  Optional<User> findById(String id);

  Optional<User> findByPubId(String pubId);

  User save(User user);

  boolean existsByPhoneNumber(String phoneNumber);

  User createBasicUser(RegistrationRequest registerRequest);

  void requestNewRole(User user, NewRoleRequest request);

  void upgradeRole(User user, NewRoleRequest request);

  void addFavoriteShop(User user, String shopId);

  void removeFavoriteShop(User user, String shopId);

  void addFavoriteProduct(User user, String shopId, String productId);

  void removeFavoriteProduct(User user, String shopId, String productId);

  Slice<Shop> findFavoriteShops(User user, Pageable pageable);

  Slice<FavoriteProduct> findFavoriteProducts(User user, Pageable pageable);
}
