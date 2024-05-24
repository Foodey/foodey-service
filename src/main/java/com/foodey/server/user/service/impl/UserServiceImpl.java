package com.foodey.server.user.service.impl;

import com.foodey.server.auth.dto.RegistrationRequest;
import com.foodey.server.exceptions.NewRoleRequestAlreadySentException;
import com.foodey.server.product.model.Product;
import com.foodey.server.product.repository.ProductRepository;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.repository.ShopRepository;
import com.foodey.server.user.model.User;
import com.foodey.server.user.model.decorator.NewRoleRequest;
import com.foodey.server.user.model.decorator.SellerRoleDecorator;
import com.foodey.server.user.model.decorator.SellerRoleRequest;
import com.foodey.server.user.repository.NewRoleRequestRepository;
import com.foodey.server.user.repository.UserRepository;
import com.foodey.server.user.service.UserService;
import java.lang.reflect.Field;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final NewRoleRequestRepository newRoleRequestRepository;
  private final ShopRepository shopRepository;
  private final ProductRepository productRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public Optional<User> findByPhoneNumber(String phoneNumber) {
    return userRepository.findByPhoneNumber(phoneNumber);
  }

  @Override
  public Optional<User> findById(String id) {
    return userRepository.findById(id);
  }

  @Override
  public Optional<User> findByPubId(String pubId) {
    return userRepository.findByPubId(pubId);
  }

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  @Override
  public boolean existsByPhoneNumber(String phoneNumber) {
    return userRepository.existsByPhoneNumber(phoneNumber);
  }

  @Override
  public User createBasicUser(RegistrationRequest request) {
    return User.builder()
        .phoneNumber(request.getPhoneNumber())
        .password(passwordEncoder.encode(request.getPassword()))
        .name(request.getName())
        .build();
  }

  @Override
  public void requestNewRole(User user, NewRoleRequest request) {
    if (user.hasRole(request.getRole())
        || newRoleRequestRepository.existsByUserIdAndRole(user.getId(), request.getRole()))
      throw new NewRoleRequestAlreadySentException(request.getRole());

    validateNewRoleRequest(user, request);
    request.setUserId(user.getId());
    newRoleRequestRepository.save(request);
  }

  @Override
  public void upgradeRole(User user, NewRoleRequest request) {
    if (request instanceof SellerRoleRequest) {
      userRepository.save(new SellerRoleDecorator(user, request).registerRole());
    }
  }

  private void validateNewRoleRequest(User user, NewRoleRequest request) {
    if (request instanceof NewRoleRequest) return;

    try {
      BindingResult bindingResult = new BindException(request, "newRoleRequest");
      if (user.getProfiles() == null) {
        for (Field field : request.getClass().getDeclaredFields()) {
          field.setAccessible(true);
          if (field.get(request) == null) {
            String key = field.getName();
            bindingResult.rejectValue(key, "required", "Field " + key + " is required");
          }
        }
      } else {
        for (Field field : request.getClass().getDeclaredFields()) {
          field.setAccessible(true);
          String key = field.getName();
          if (user.getProfiles().get(key) == null && field.get(request) == null) {
            bindingResult.rejectValue(key, "required", "Field " + key + " is required");
          }
        }
      }
      if (bindingResult.hasErrors()) throw new RuntimeException(new BindException(bindingResult));
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void addFavoriteShop(User user, String shopId) {
    user.getFavoriteShopIds().add(shopId);
    userRepository.save(user);
  }

  @Override
  public void removeFavoriteShop(User user, String shopId) {
    user.getFavoriteShopIds().remove(shopId);
    userRepository.save(user);
  }

  @Override
  public void addFavoriteProduct(User user, String productId) {
    user.getFavoriteProductIds().add(productId);
    userRepository.save(user);
  }

  @Override
  public void removeFavoriteProduct(User user, String productId) {
    user.getFavoriteProductIds().remove(productId);
    userRepository.save(user);
  }

  @Override
  public Slice<Shop> findFavoriteShops(User user, Pageable pageable) {
    return shopRepository.findByIdIn(user.getFavoriteShopIds(), pageable);
  }

  @Override
  public Slice<Product> findFavoriteProducts(User user, Pageable pageable) {
    return productRepository.findByIdIn(user.getFavoriteProductIds(), pageable);
  }
}
