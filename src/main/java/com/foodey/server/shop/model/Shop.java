package com.foodey.server.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodey.server.validation.annotation.OptimizedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "shops")
@JsonIgnoreProperties(
    value = {"id", "menus", "ownerId", "createdAt", "updatedAt", "rating", "categories"},
    allowGetters = true)
@CompoundIndex(def = "{'name': 1, 'branchId': 1}", name = "shop_name_branch_id")
@ToString
@NoArgsConstructor
public class Shop implements Persistable<String> {

  @Null @Id private String id;

  @NotBlank private String branchId;

  @Null private String ownerId;

  @OptimizedName private String name;

  private String logo;
  private String wallpaper;

  private long rating = 0;

  @NotBlank
  @Size(min = 2, max = 100)
  private String address;

  private List<@Valid ShopMenu> menus = new ArrayList<>();

  @Indexed(name = "shop_categories")
  private Set<String> categories = new HashSet<>();

  @JsonIgnore @CreatedDate private Instant createdAt;

  @JsonIgnore @LastModifiedDate private Instant updatedAt;

  public Shop(
      String name, String address, String logo, String wallpaper, String branchId, String ownerId) {
    this.name = name;
    this.address = address;
    this.logo = logo;
    this.wallpaper = wallpaper;
    this.branchId = branchId;
  }

  @Override
  public boolean isNew() {
    return createdAt == null || id == null;
  }
}
