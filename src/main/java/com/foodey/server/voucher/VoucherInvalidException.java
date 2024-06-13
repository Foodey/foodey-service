package com.foodey.server.voucher;

import com.foodey.server.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class VoucherInvalidException extends BaseException {

  public VoucherInvalidException(String message) {
    super(HttpStatus.FORBIDDEN, message);
  }
}
