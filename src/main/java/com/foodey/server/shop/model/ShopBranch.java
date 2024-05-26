package com.foodey.server.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodey.server.validation.annotation.OptimizedName;
import com.foodey.server.validation.annotation.PhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@JsonIgnoreProperties(
    value = {"id", "ownerId"},
    allowGetters = true)
public class ShopBranch implements ShopMenusContainer {
  @Null @Id private String id;

  @OptimizedName private String name;

  @PhoneNumber private String phoneNumber;

  @Email private String email;

  @Null @JsonIgnore private String ownerId;

  private String logo;

  private String wallpaper;

  // the list of menus apply to all shop of the same branch
  @JsonIgnore @Default private List<@Valid ShopMenu> menus = new ArrayList<>();
}
