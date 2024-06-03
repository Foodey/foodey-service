package com.foodey.server.validation.annotation;

import com.foodey.server.validation.validator.MatchPasswordValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.messaging.handler.annotation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchPasswordValidator.class)
@Documented
public @interface MatchPassword {
  String message() default "The new passwords must match with confirm password.";

  Class<?>[] groups() default {};

  // The password can be empty (no need password)
  boolean allowedEmpty() default false;

  Class<? extends Payload>[] payload() default {};
}
