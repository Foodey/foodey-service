package com.foodey.server.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_favorite_products")
public class UserFavoriteProduct {

  @Id private String id;
  private String userId;
  private String productId;
  private String shopId;
  private String menuId;

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    else if (!(obj instanceof UserFavoriteProduct)) return false;
    return productId.equals(((UserFavoriteProduct) obj).productId)
        && shopId.equals(((UserFavoriteProduct) obj).shopId)
        && menuId.equals(((UserFavoriteProduct) obj).menuId);
  }
}
