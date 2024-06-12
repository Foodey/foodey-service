package com.foodey.server.validation.annotation;

import com.foodey.server.validation.validator.VoucherValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VoucherValidator.class)
@Documented
public @interface ValidVoucher {

  String message() default "The voucher is not valid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
