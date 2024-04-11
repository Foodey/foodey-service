package com.foodey.server.user;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.foodey.server.validation.annotation.OptimizedName;
import com.foodey.server.validation.annotation.Password;
import com.foodey.server.validation.annotation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
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
public class User implements UserDetails, UserRole {

  @JsonIgnore @Id private String id;

  // Why we need this?
  // The public id is a unique identifier for the account.
  // It is used to identify the account in the system.
  // It can be shared to anyone and it is not sensitive information.
  // Why don't use phoneNumber as the public id?
  // Because the phoneNumber can be changed by the user,
  // and we need a unique identifier for the account.
  @Default
  @Indexed
  @Schema(description = "The public id of the account")
  private String pubId = NanoIdUtils.randomNanoId();

  @PhoneNumber
  @Indexed
  @Schema(description = "The phone number of the account", required = true)
  private String phoneNumber;

  @JsonIgnore
  @Password
  @Schema(description = "The password of the account", required = true)
  private String password;

  @OptimizedName
  @Schema(description = "The name of the account", required = true)
  private String name;

  @Default
  @Schema(description = "The avatar of the account")
  private String avatar = "";

  @Schema(description = "Some new neccecary fields when adding new role")
  private Map<String, Object> profiles;

  @Default
  @Schema(description = "The status of the account")
  private UserStatus status = UserStatus.UNVERIFIED;

  @JsonIgnore
  @Default
  @Schema(description = "The roles of the account")
  private Set<Role> roles = Set.of(new Role(RoleType.CUSTOMER));

  @Default
  @CreatedDate
  @Schema(description = "The created time of the account")
  private Instant createdAt = Instant.now();

  @Default
  @LastModifiedDate
  @Schema(description = "The updated time of the account")
  private Instant updatedAt = Instant.now();

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
                role.getPermissions()
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
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return phoneNumber;
    // return pubId;
  }

  @Override
  public boolean isAccountNonExpired() {
    return status != UserStatus.ARCHIVED && status != UserStatus.DELETED;
  }

  @Override
  public boolean isAccountNonLocked() {
    return status != UserStatus.BANNED;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return status != UserStatus.COMPROMISED;
  }

  @Override
  public boolean isEnabled() {
    return status == UserStatus.ACTIVE;
  }

  @Override
  public User upgradeRole() {
    roles.add(new Role(RoleType.ADMIN));
    return this;
  }
}
