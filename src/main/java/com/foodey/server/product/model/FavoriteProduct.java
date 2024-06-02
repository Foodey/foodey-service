package com.foodey.server.product.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteProduct {

  @AllArgsConstructor
  @Getter
  public static class Identity implements Serializable {
    private String productId;
    private String shopId;

    @Override
    public int hashCode() {
      return productId.hashCode() + shopId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      else if (obj instanceof Identity) {
        Identity other = (Identity) obj;
        return productId.equals(other.productId) && shopId.equals(other.shopId);
      }
      return false;
    }
  }

  private Identity fpId;

  private Product product;

  public FavoriteProduct(Product product, String shopId, String productId) {
    this.product = product;
    this.fpId = new Identity(productId, shopId);
  }

  public FavoriteProduct(Product product, Identity fpId) {
    this.product = product;
    this.fpId = fpId;
  }
}
