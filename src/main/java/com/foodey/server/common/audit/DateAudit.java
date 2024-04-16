package com.foodey.server.common.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

@JsonIgnoreProperties(
    value = {"createdAt", "updatedAt"},
    allowGetters = false)
public interface DateAudit extends Serializable {

  @CreatedDate
  @Field("createdAt")
  Instant getCreatedAt();

  @LastModifiedDate
  @Field("updatedAt")
  Instant getUpdatedAt();
}
