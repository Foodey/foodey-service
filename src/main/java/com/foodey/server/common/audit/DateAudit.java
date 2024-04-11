package com.foodey.server.common.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
@SuperBuilder
@JsonIgnoreProperties(
    value = {"createdAt", "updatedAt"},
    allowGetters = true)
public abstract class DateAudit implements Serializable {

  @Default @CreatedDate private Instant createdAt = Instant.now();

  @Default @LastModifiedDate private Instant updatedAt = Instant.now();
}
