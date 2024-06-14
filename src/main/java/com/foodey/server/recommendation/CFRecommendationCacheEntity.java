package com.foodey.server.recommendation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash(value = "cfrecommendation", timeToLive = 24 * 60 * 60)
public class CFRecommendationCacheEntity {

  @Id private String userId;

  private Point location;

  public CFRecommendationCacheEntity(String userId, Point location) {
    this.userId = userId;
    this.location = location;
  }
}
