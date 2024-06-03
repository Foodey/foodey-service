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
  private String shopId;

  public MenuSizeTooBigException(int maxSize, String shopId) {
    super("Menu size is too big for shop with id: " + shopId + ". Max size is: " + maxSize);

    this.maxSize = maxSize;
    this.shopId = shopId;
  }
}
