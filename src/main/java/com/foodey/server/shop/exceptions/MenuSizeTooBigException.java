package com.foodey.server.shop.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class MenuSizeTooBigException extends RuntimeException {

  private int maxSize;

  public MenuSizeTooBigException(int maxSize) {
    super(String.format("Menu size is too big, Max size allowed: %d", maxSize));
  }
}
