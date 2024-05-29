package com.foodey.server.validation.annotation;

import com.foodey.server.common.regexp.PhoneNumberRegexp;
import com.foodey.server.validation.validator.PhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
@Documented
public @interface PhoneNumber {
  String message() default "Invalid phone number";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  boolean optional() default false;

  String regexp() default "";

  PhoneNumberRegexp[] regions() default {};
}
