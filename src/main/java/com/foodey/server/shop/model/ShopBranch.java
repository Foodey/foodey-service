package com.foodey.server.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodey.server.validation.annotation.OptimizedName;
import com.foodey.server.validation.annotation.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document(collection = "shop_branches")
@CompoundIndex(name = "name_owner_id", def = "{'name': 1, 'ownerId': 1}")
@AllArgsConstructor
public class ShopBranch {
  @Null @Id private String id;

  @OptimizedName private String name;

  @PhoneNumber private String phoneNumber;

  @Email private String email;

  @JsonIgnore private String ownerId;

  private String logo;

  private String wallpaper;
}
