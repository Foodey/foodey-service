package com.foodey.server.shop.model;

import com.foodey.server.validation.annotation.OptimizedName;
import com.foodey.server.validation.annotation.PhoneNumber;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Document(collection = "shop_branches")
@CompoundIndexes({
  @CompoundIndex(name = "shopbranch_name_owner_id", def = "{'name': 1, 'ownerId': 1}")
})
public class ShopBranch {
  @Id private String id;

  @OptimizedName private String name;

  @PhoneNumber private String phone;

  @Email private String email;

  private String ownerId;

  private String logo;

  private String wallpaper;
}
