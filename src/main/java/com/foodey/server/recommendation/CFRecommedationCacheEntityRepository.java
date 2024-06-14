package com.foodey.server.recommendation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CFRecommedationCacheEntityRepository
    extends CrudRepository<CFRecommendationCacheEntity, String> {}
