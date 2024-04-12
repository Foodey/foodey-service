package com.foodey.server.auth.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "refresh_tokens")
@Schema(name = "RefreshToken", description = "Refresh token")
public class RefreshToken {
  @Transient private static final long serialVersionUID = 1L;

  @Id private String id;

  @JsonIgnore
  @Schema(
      description = "User public ID of the user associated with the refresh token",
      required = true)
  private String userPubId;

  @Schema(description = "Refresh token value", required = true)
  @Indexed
  private String token;

  @Default
  @Schema(description = "Indicates whether the refresh token is revoked")
  private boolean revoked = false;

  @Schema(description = "Date and time when the refresh token expires")
  private Instant expiresAt;

  @Schema(description = "Date and time when the refresh token was revoked")
  private Instant revokedAt;

  @Default @CreatedDate private Instant createdAt = Instant.now();
  @Default @LastModifiedDate private Instant updatedAt = Instant.now();

  public RefreshToken(String userPubId, Instant expiresAt) {
    this.revoked = false;
    this.userPubId = userPubId;
    this.expiresAt = expiresAt;
    this.token = NanoIdUtils.randomNanoId();
  }

  public RefreshToken(String userPubId, long expirationTimeInMs) {
    this(userPubId, Instant.now().plusMillis(expirationTimeInMs));
  }

  public boolean isExpired() {
    return Instant.now().isAfter(expiresAt);
  }

  public boolean isActive() {
    return !isExpired() && !revoked;
  }

  public RefreshToken refresh() {
    token = NanoIdUtils.randomNanoId();
    return this;
  }

  public RefreshToken revoke() {
    if (!revoked) {
      revoked = true;
      revokedAt = Instant.now();
    }
    return this;
  }
}
