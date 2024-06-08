// package com.foodey.server.comment.service.impl;

// import com.foodey.server.comment.dto.CommentRequest;
// import com.foodey.server.comment.model.BaseComment;
// import com.foodey.server.comment.service.CommentService;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Slice;

// // @Service
// // @RequiredArgsConstructor
// public class CommentServiceImpl implements CommentService {

//   // private final BaseCommentRepository baseCommentRepository;

//   @Override
//   public void addComment(CommentRequest commentRequest) {
//     if (commentRequest.isRepliedComment()) {

//     } else {

//     }
//   }

//   @Override
//   public Slice<BaseComment> getComments(String shopId, String productId, Pageable pageable) {
//     // return baseCommentRepository.findByShopIdAndProductId(shopId, productId, pageable);
//   }

//   @Override
//   public Slice<BaseComment> getRepliedOfPathComments(
//       String shopId, String productId, String commentId) {
//     // TODO Auto-generated method stub
//     throw new UnsupportedOperationException("Unimplemented method 'getRepliedOfPathComments'");
//   }
// }
