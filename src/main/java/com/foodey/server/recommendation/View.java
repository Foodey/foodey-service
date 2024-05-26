package com.foodey.server.recommendation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(value = "views")
public class View {

  private String userId;

  private String shopId;

  private String menuId;

  private String productId;

  private long viewCount;
}
