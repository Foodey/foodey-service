package com.foodey.server.validation.validator;

import com.foodey.server.validation.annotation.ValidVoucher;
import com.foodey.server.voucher.Voucher;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class VoucherValidator implements ConstraintValidator<ValidVoucher, Voucher> {

  @Override
  public void initialize(ValidVoucher constraintAnnotation) {}

  @Override
  public boolean isValid(Voucher voucher, ConstraintValidatorContext context) {

    if (voucher == null) {
      return false;
    }

    return true;
  }
}
