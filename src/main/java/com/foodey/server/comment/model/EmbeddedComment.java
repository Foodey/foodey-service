// package com.foodey.server.comment.model;

// import java.util.ArrayList;
// import java.util.List;
// import lombok.AccessLevel;
// import lombok.AllArgsConstructor;
// import lombok.Builder.Default;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import lombok.experimental.SuperBuilder;

// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @SuperBuilder
// public class EmbeddedComment extends BaseComment {

//   public EmbeddedComment(
//       String shopId,
//       String productId,
//       String parentCommentId,
//       String content,
//       String authorName,
//       String authorId) {
//     super(shopId, productId, parentCommentId, content, authorName, authorId,
// CommentType.EMBEDDED);
//   }

//   @Setter(AccessLevel.NONE)
//   @Default
//   private List<BaseComment> replyComments = new ArrayList<>();

//   @Override
//   public long getTotalReplies() {
//     return replyComments.size()
//         + replyComments.stream().mapToLong(BaseComment::getTotalReplies).sum();
//   }
// }
