package com.foodey.server.shop.repository;

import com.foodey.server.shop.model.Shop;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends MongoRepository<Shop, String> {

  boolean existsByNameAndBrandId(String name, String brandId);

  Optional<Shop> findByIdAndOwnerId(String id, String ownerId);

  Optional<Shop> findByIdAndBrandIdAndOwnerId(String id, String brandId, String ownerId);

  Optional<Shop> findByIdAndBrandId(String id, String brandId);

  Slice<Shop> findByIdInAndAddressCoordsNear(
      Iterable<String> ids, Point point, Distance distance, Pageable pageable);

  List<Shop> findByBrandId(String brandId);

  Slice<Shop> findByBrandId(String brandId, Pageable pageable);

  Slice<Shop> findByCategoryIdsContaining(String categoryId, Pageable pageable);

  Slice<Shop> findByCategoryIdsContainingAndAddressCoordsNear(
      String categoryId, Point point, Distance distance, Pageable pageable);

  Slice<Shop> findByIdIn(Iterable<String> ids, Pageable pageable);

  Slice<Shop> findByNameContainingIgnoreCase(String name, Pageable pageable);

  Slice<Shop> findByNameContainingIgnoreCaseAndAddressCoordsNear(
      String name, Point point, Distance distance, Pageable pageable);

  Slice<Shop> findByOwnerId(String userId, Pageable pageable);

  @Aggregation(
      pipeline = {"{ $match: { lastRatingCalculationAt: { $lt: ?0 } } }", "{ $limit: ?1 }"})
  List<Shop> findByLastRatingCalculationAtBeforeLimit(Instant lastRatingCalculation, long limit);

  Slice<Shop> findByAddressCoordsNear(Point point, Distance distance, Pageable pageable);
}
