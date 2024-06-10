package com.foodey.server.recommendation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

@Getter
@Setter
public class Interaction implements Persistable<String> {

  @Id private String id;

  private String userId;

  private String itemId;

  @CreatedDate private Instant createdAt;

  @Override
  @JsonIgnore
  public boolean isNew() {
    return createdAt == null || id == null;
  }
}
