package com.foodey.server.shop.model;

import com.esotericsoftware.kryo.serializers.FieldSerializer.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodey.server.validation.annotation.OptimizedName;
import com.foodey.server.validation.annotation.PhoneNumber;
import com.mongodb.lang.NonNull;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@CompoundIndex(name = "name_owner_id", def = "{'name': 1, 'ownerId': 1}")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(
    value = {"id", "ownerId", "createdAt", "updatedAt", "menu"},
    allowGetters = true)
@Document(collection = "shop_brands")
public class ShopBrand implements Persistable<String> {
  @Id
  @Schema(description = "The unique identifier of the shop brand.")
  private String id;

  @Schema(description = "The name of the shop brand.")
  @OptimizedName
  private String name;

  @Schema(description = "The phone number of the shop brand.")
  @PhoneNumber
  private String phoneNumber;

  @Schema(description = "The email of the shop brand.")
  @Email
  private String email;

  @JsonIgnore
  @Schema(description = "The address of the shop brand.")
  @NotNull
  @NonNull
  private String ownerId;

  @Schema(description = "The logo of the shop brand.")
  private String logo;

  @Schema(description = "The wallpaper of the shop brand.")
  private String wallpaper;

  @Schema(description = "The menu of the shop brand.")
  @Setter(AccessLevel.NONE)
  @Default
  @JsonIgnore
  private Menu menu = new Menu();

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;

  @Override
  @JsonIgnore
  public boolean isNew() {
    return createdAt == null;
  }
}
