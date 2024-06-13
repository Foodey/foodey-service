package com.foodey.server.user.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.foodey.server.common.model.CloudinaryImage;
import com.foodey.server.common.model.CloudinaryImageManager;
import com.foodey.server.product.model.FavoriteProduct;
import com.foodey.server.user.enums.RoleType;
import com.foodey.server.user.enums.UserStatus;
import com.foodey.server.user.model.decorator.UserRole;
import com.foodey.server.validation.annotation.OptimizedName;
import com.foodey.server.validation.annotation.Password;
import com.foodey.server.validation.annotation.PhoneNumber;
import com.webauthn4j.data.attestation.authenticator.AuthenticatorData;
import com.webauthn4j.data.extension.authenticator.ExtensionAuthenticatorOutput;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@SuperBuilder
@Schema(description = "User model", name = "User")
@Document(collection = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class User implements UserDetails, UserRole, Persistable<String>, CloudinaryImageManager {
  @Transient private static final String USER_AVATAR_FOLDER = "user-avatars";

  @JsonIgnore @Id private String id;

  // Why we need this?
  // The public id is a unique identifier for the account.
  // It is used to identify the account in the system.
  // It can be shared to anyone and it is not sensitive information.
  // Why don't use phoneNumber as the public id?
  // Because the phoneNumber can be changed by the user,
  // and we need a unique identifier for the account.
  @Default
  @Indexed(unique = true)
  @Schema(description = "The public id of the account")
  private String pubId = NanoIdUtils.randomNanoId();

  @PhoneNumber
  @Indexed(unique = true)
  @Schema(description = "The phone number of the account", requiredMode = RequiredMode.REQUIRED)
  private String phoneNumber;

  @JsonIgnore
  @Password
  @Schema(description = "The password of the account", requiredMode = RequiredMode.REQUIRED)
  private String password;

  @OptimizedName
  @Schema(description = "The name of the account", requiredMode = RequiredMode.REQUIRED)
  private String name;

  @Email
  @Schema(description = "The email of the user", requiredMode = RequiredMode.REQUIRED)
  private String email;

  public String getCloudinaryAvatarFolder() {
    return USER_AVATAR_FOLDER;
  }

  @Default private CloudinaryImage cldAvatar = new CloudinaryImage(USER_AVATAR_FOLDER);

  @Schema(description = "The avatar of the account")
  public String getAvatar() {
    return cldAvatar.getUrl();
  }

  @Schema(description = "The last logout time of the account")
  private Instant lastLogoutAt;

  @Schema(description = "Some new neccecary fields when adding new role")
  private Map<String, Object> profiles;

  @Default
  @Schema(description = "The status of the account")
  private UserStatus status = UserStatus.UNVERIFIED;

  @Default
  @Schema(description = "The roles of the account")
  private Set<Role> roles = Set.of(new Role(RoleType.CUSTOMER));

  @Default
  @Setter(AccessLevel.NONE)
  @Schema(description = "The favorite product ids of the account")
  private Set<FavoriteProduct.Identity> favoriteProductIds = new LinkedHashSet<>();

  @JsonIgnore
  public boolean addFavoriteProduct(String shopId, String productId) {
    return favoriteProductIds.add(new FavoriteProduct.Identity(productId, shopId));
  }

  @JsonIgnore
  public boolean removeFavoriteProduct(String shopId, String productId) {
    return favoriteProductIds.removeIf(
        fdi -> fdi.getProductId().equals(productId) && fdi.getShopId().equals(shopId));
  }

  @Schema(description = "The favorite shop ids of the account")
  @Default
  private Set<String> favoriteShopIds = new HashSet<>();

  @Schema(description = "The list of voucher that the user colleted")
  @Default
  private Set<String> voucherCodes = new HashSet<>();

  @Schema(description = "The list of credential ids of the account")
  @Getter
  @Default
  @JsonIgnore
  private Set<AuthenticatorData<ExtensionAuthenticatorOutput>> authenticatorDatas = new HashSet<>();

  @CreatedDate
  @Schema(description = "The created time of the account")
  @JsonIgnore
  private Instant createdAt;

  @LastModifiedDate
  @Schema(description = "The updated time of the account")
  @JsonIgnore
  private Instant updatedAt;

  @JsonIgnore @Transient private Collection<? extends GrantedAuthority> authorities;

  public boolean hasRole(RoleType roleType) {
    return roles.stream().anyMatch(r -> r.getName().equals(roleType));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (roles == null) {
      return Collections.emptySet();
    } else {
      if (authorities == null || authorities.isEmpty()) {
        Collection<SimpleGrantedAuthority> auths = new HashSet<>();
        roles.forEach(
            role -> {
              auths.add(new SimpleGrantedAuthority("ROLE_" + role.getName().name()));
              if (role.getPermissions() != null) {
                role.getPermissions().stream()
                    .forEach(
                        permission -> {
                          auths.add(new SimpleGrantedAuthority(permission.name()));
                        });
              }
            });
        authorities = auths;
      }
      return authorities;
    }
  }

  @Override
  public Map<String, Object> getProfiles() {
    return profiles;
  }

  @Override
  public Set<Role> getRoles() {
    return roles;
  }

  @Override
  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return phoneNumber;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return status != UserStatus.ARCHIVED && status != UserStatus.DELETED;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return status != UserStatus.BANNED;
  }

  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return status != UserStatus.COMPROMISED;
  }

  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return status == UserStatus.ACTIVE;
  }

  @Override
  public User registerRole() {
    roles.add(new Role(RoleType.CUSTOMER));
    return this;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    else if (obj instanceof User) {
      User that = (User) obj;
      return id.equals(that.id) || phoneNumber.equals(that.phoneNumber) || pubId.equals(that.pubId);
    }
    return false;
  }

  @Override
  @JsonIgnore
  public boolean isNew() {
    return createdAt == null || id == null;
  }
}
