// package com.foodey.server.comment.model;

// import com.fasterxml.jackson.annotation.JsonTypeInfo;
// import com.mongodb.lang.NonNull;
// import io.swagger.v3.oas.annotations.media.Schema;
// import jakarta.validation.constraints.NotBlank;
// import java.time.Instant;
// import lombok.AllArgsConstructor;
// import lombok.Builder.Default;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import lombok.experimental.SuperBuilder;
// import org.bson.codecs.pojo.annotations.BsonDiscriminator;
// import org.springframework.data.annotation.CreatedDate;
// import org.springframework.data.annotation.Id;
// import org.springframework.data.annotation.LastModifiedDate;
// import org.springframework.data.domain.Persistable;
// import org.springframework.data.mongodb.core.index.CompoundIndex;
// import org.springframework.data.mongodb.core.index.CompoundIndexes;
// import org.springframework.data.mongodb.core.mapping.Document;

// @Getter
// @Setter
// @SuperBuilder
// @AllArgsConstructor
// @NoArgsConstructor
// @JsonTypeInfo(
//     include = JsonTypeInfo.As.WRAPPER_OBJECT,
//     use = JsonTypeInfo.Id.NAME,
//     property = "type",
//     defaultImpl = EmbeddedComment.class)
// @BsonDiscriminator
// @Document(collection = "comments")
// @CompoundIndexes({
//   @CompoundIndex(name = "shop_product_idx", def = "{'shopId': 1, 'productId': 1}"),
// })
// @Schema(name = "Comment", description = "Comment model")
// public abstract class BaseComment implements Persistable<String> {

//   @Id
//   @Schema(description = "Comment ID", example = "1234567890")
//   private String id;

//   @Schema(description = "Shop ID", example = "1234567890")
//   @NonNull
//   private String shopId;

//   @Schema(description = "Product ID", example = "1234567890")
//   @NonNull
//   private String productId;

//   @Schema(description = "Comment content", example = "This is a comment")
//   @NonNull
//   @NotBlank
//   private String content;

//   @Schema(description = "Nearest parent comment ID", example = "1234567890")
//   private String directParentId;

//   public boolean hasDirectParent() {
//     return directParentId != null;
//   }

//   public BaseComment(
//       String shopId,
//       String productId,
//       String parentCommentId,
//       String content,
//       String authorName,
//       String authorId,
//       CommentType type) {
//     this.shopId = shopId;
//     this.productId = productId;
//     this.directParentId = parentCommentId;
//     this.content = content;
//     this.authorName = authorName;
//     this.authorId = authorId;
//     this.type = type;
//   }

//   private CommentType type;

//   /**
//    * This use to determine the type of comment In case of USUAL, it is a normal comment with
// nested
//    * comments allowed In case of UNUSUAL, it has many nested comments but it is not allowed to
// have
//    * nested comments in this document and we need to handle it by other way e.g
//    *
// https://viblo.asia/p/nestjs-coding-practice-mot-vai-cach-trien-khai-comments-feature-voi-mongodb-vlZL9owM4QK
//    *
//    * <p>USUAL: usual comment
//    *
//    * <p>UNUSUAL: unusual comment
//    */
//   private boolean replied = false;

//   @Schema(description = "Total liked", example = "10")
//   @Default
//   private long totalLiked = 0;

//   @Schema(description = "Total disliked", example = "10")
//   @Default
//   private long totalDisliked = 0;

//   @NonNull
//   @Schema(description = "Comment author name", example = "John Doe")
//   private String authorName;

//   @NonNull
//   @Schema(description = "Comment author id", example = "1234567890")
//   private String authorId;

//   @CreatedDate private Instant createdAt;

//   @LastModifiedDate private Instant updatedAt;

//   public abstract long getTotalReplies();

//   @Override
//   public boolean isNew() {
//     return createdAt == null || id == null;
//   }

//   public void increaseTotalLiked() {
//     totalLiked++;
//   }

//   public void increaseTotalDisliked() {
//     totalDisliked++;
//   }

//   public void decreaseTotalLiked() {
//     totalLiked--;
//   }

//   public void decreaseTotalDisliked() {
//     totalDisliked--;
//   }
// }
