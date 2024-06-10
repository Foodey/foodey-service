package com.foodey.server.evaluation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "The evaluation of a restaurant")
@Document(collection = "evaluations")
@JsonIgnoreProperties(
    value = {"id", "creatorName", "creatorId", "reply", "createdAt", "updatedAt", "totalLikes"},
    allowGetters = true)
@JsonTypeInfo(
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    visible = true,
    defaultImpl = OrderEvaluation.class)
@JsonSubTypes({
  @JsonSubTypes.Type(value = OrderEvaluation.class, name = EvaluationType.Fields.ORDER),
  @JsonSubTypes.Type(value = ProductEvaluation.class, name = EvaluationType.Fields.PRODUCT)
})
@BsonDiscriminator
public abstract class BaseEvaluation implements Persistable<String> {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Reply {
    private String content;
    @CreatedDate private Instant createdAt;
    @LastModifiedDate private Instant updatedAt;
  }

  @Id
  @Schema(
      description = "The unique identifier of the evaluation",
      example = "60f1b9b7b3b3b3b3b3b3b3b3")
  private String id;

  private EvaluationType type;

  public BaseEvaluation(EvaluationType type) {
    this.type = type;
  }

  @Schema(description = "The rating of the evaluation, from 1 to 5", example = "5")
  @Min(1)
  @Max(5)
  private Byte rating;

  @Schema(description = "Whether the evaluation is rated by the user", example = "false")
  // thoi diem schedule job thuc hien rating thi truong nay se duoc set la true
  private boolean rated = false;

  @Schema(description = "The comment of the evaluation", example = "The food was delicious!")
  private String comment;

  @Schema(description = "The total number of likes of the evaluation", example = "10")
  private long totalLikes = 0;

  @Schema(description = "The user name who created the evaluation", example = "John Doe")
  // why we need to store the creator name in the evaluation?
  // Because the name is no need to exactly match the user name in the user document.
  private String creatorName;

  @Schema(
      description = "The user id who created the evaluation",
      example = "60f1b9b7b3b3b3b3b3b3b3")
  private String creatorId;

  @Schema(description = "The reply of the seller to the evaluation")
  private Reply reply;

  @Schema(description = "The date and time when the evaluation was created")
  @CreatedDate
  private Instant createdAt;

  @Schema(description = "The date and time when the evaluation was last updated")
  @LastModifiedDate
  private Instant updatedAt;

  public BaseEvaluation(
      EvaluationType type, byte rating, String comment, String creatorName, String creatorId) {
    this.type = type;
    this.rating = rating;
    this.comment = comment;
    this.creatorName = creatorName;
    this.creatorId = creatorId;
  }

  @Override
  @JsonIgnore
  public boolean isNew() {
    return createdAt == null || id == null;
  }
}
