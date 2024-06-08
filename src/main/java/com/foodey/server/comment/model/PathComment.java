// package com.foodey.server.comment.model;

// import java.util.List;
// import org.springframework.data.annotation.Transient;

// /** PathComment */
// public class PathComment extends BaseComment {

//   private String path;

//   @Transient private List<BaseComment> repliedComments;

//   public PathComment(String path) {}

//   @Override
//   public long getTotalReplies() {
//     return repliedComments.size()
//         + repliedComments.stream().mapToLong(BaseComment::getTotalReplies).sum();
//   }
// }
