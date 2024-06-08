package com.foodey.server.comment.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName
public enum CommentType {
  PATH,
  EMBEDDED,
}
