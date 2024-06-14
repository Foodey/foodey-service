package com.foodey.server.shop.service.impl;

import com.foodey.server.exceptions.ResourceAlreadyInUseException;
import com.foodey.server.exceptions.ResourceNotFoundException;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.repository.ShopRepository;
import com.foodey.server.shop.service.ShopBrandService;
import com.foodey.server.shop.service.ShopService;
import com.foodey.server.upload.service.CloudinaryService;
import com.foodey.server.user.model.User;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.geo.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

  private final ShopRepository shopRepository;
  private final ShopBrandService shopBrandService;
  private final CloudinaryService cloudinaryService;

  @Override
  public Shop createShop(Shop shop, User user) {
    try {
      String brandId = shop.getBrandId();
      String userId = user.getId();

      shopBrandService.verifyOwner(brandId, userId);
      shop.setOwnerId(userId);
      shop.setBrandId(brandId);
      Shop newShop = shopRepository.save(shop);

      newShop.setLogoUploadApiOptions(cloudinaryService.getUploadApiOptions(newShop.getCldLogo()));
      newShop.setWallpaperUploadApiOptions(
          cloudinaryService.getUploadApiOptions(newShop.getCldWallpaper()));
      return newShop;
    } catch (DuplicateKeyException e) {
      throw new ResourceAlreadyInUseException("Shop", "name", shop.getName());
    }
  }

  @Override
  @Cacheable(value = "shops", key = "#id")
  public Shop findById(String id) {
    return shopRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Shop", "id", id));
  }

  @Override
  @Cacheable(value = "shops", key = "#pageable.pageNumber")
  public Slice<Shop> findAll(Pageable pageable) {
    return shopRepository.findAll(pageable);
  }

  @Override
  public Slice<Shop> findAllNear(
      double longitude, double latitude, long maxDistanceKms, Pageable pageable) {
    return shopRepository.findByAddressCoordsNear(
        new Point(longitude, latitude), new Distance(maxDistanceKms, Metrics.KILOMETERS), pageable);
  }

  @Override
  @CachePut(value = "shops", key = "#shop.id")
  @CacheEvict(value = "shops", allEntries = true)
  public Shop save(Shop shop) {
    return shopRepository.save(shop);
  }

  @Override
  @CacheEvict(value = "shops", allEntries = true)
  public List<Shop> saveAll(Iterable<Shop> shops) {
    return shopRepository.saveAll(shops);
  }

  @Override
  @Caching(
      cacheable = {
        @Cacheable(
            value = "shops",
            key = "#categoryId.concat('-').concat(#pageable.pageNumber)",
            cacheManager = "caffeineCacheManager"),
        // @Cacheable(
        //     value = "shops",
        //     key = "#categoryId.concat('-').concat(#pageable.pageNumber)",
        //     cacheManager = "redisCacheManager"),
      })
  public Slice<Shop> findByCategoryId(String categoryId, Pageable pageable) {
    return shopRepository.findByCategoryIdsContaining(categoryId, pageable);
  }

  @Override
  @Caching(
      cacheable = {
        @Cacheable(
            value = "shops",
            key =
                "#category.concat('-').concat(#longitude).concat('-').concat(#latitude).concat('-').concat(#maxDistanceKms).concat('-').concat(#pageable.pageNumber)",
            cacheManager = "caffeineCacheManager"),
      })
  public Slice<Shop> findByCategoryIdNear(
      String category, double longitude, double latitude, long maxDistanceKms, Pageable pageable) {
    return shopRepository.findByCategoryIdsContainingAndAddressCoordsNear(
        category,
        new Point(longitude, latitude),
        new Distance(maxDistanceKms, Metrics.KILOMETERS),
        pageable);
  }

  @Override
  public Shop findByIdAndVerifyOwner(String id, String userId) {
    Shop shop = findById(id);
    if (!shop.getOwnerId().equals(userId))
      throw new AccessDeniedException("You are not owner of this shop.");
    return shop;
  }

  @Override
  @Caching(
      cacheable = {
        @Cacheable(value = "shops", cacheManager = "caffeineCacheManager"),
        @Cacheable(value = "shops", cacheManager = "redisCacheManage"),
      })
  public List<Shop> findByBrandId(String brandId) {
    return shopRepository.findByBrandId(brandId);
  }

  @Override
  public Slice<Shop> findByBrandId(String brandId, Pageable pageable) {
    return shopRepository.findByBrandId(brandId, pageable);
  }

  @Override
  public Shop findByIdAndBrandIdAndVerifyOwner(String id, String brandId, String userId) {
    Shop shop =
        shopRepository
            .findByIdAndBrandId(id, brandId)
            .orElseThrow(() -> new ResourceNotFoundException("Shop", "id", id));

    if (!shop.getOwnerId().equals(userId))
      throw new AccessDeniedException("You are not owner of this shop.");
    return shop;
  }

  @Override
  public Slice<Shop> searchByName(String query, Pageable pageable) {
    if (query.isEmpty()) {
      return new SliceImpl<>(List.of(), pageable, false);
    }
    return shopRepository.findByNameContainingIgnoreCase(query, pageable);
  }

  @Override
  public List<Shop> getShopsNotRatedSince(Instant date, long limit) {
    return shopRepository.findByLastRatingCalculationAtBeforeLimit(date, limit);
  }

  @Override
  public Slice<Shop> findByOwnerId(String ownerId, Pageable pageable) {
    return shopRepository.findByOwnerId(ownerId, pageable);
  }

  @Override
  public Map<String, Object> getLogoUploadApiOptions(String shopId, String userId) {
    Shop shop = findByIdAndVerifyOwner(shopId, userId);
    return cloudinaryService.getUploadApiOptions(shop.getCldLogo());
  }

  @Override
  public Map<String, Object> getWallpaperUploadApiOptions(String shopId, String userId) {
    Shop shop = findByIdAndVerifyOwner(shopId, userId);
    return cloudinaryService.getUploadApiOptions(shop.getCldWallpaper());
  }

  @Override
  public Slice<Shop> searchByName(
      String query, double longitude, double latitude, long maxDistanceKms, Pageable pageable) {
    return shopRepository.findByNameContainingIgnoreCaseAndAddressCoordsNear(
        query,
        new Point(longitude, latitude),
        new Distance(maxDistanceKms, Metrics.KILOMETERS),
        pageable);
  }
}
