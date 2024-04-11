package com.foodey.server.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
@NotBlank(message = "Name must be between 3 and 50 characters")
@Size(min = 2, max = 50, message = "Name must be between 3 and 50 characters")
public @interface OptimizedName {
  String message() default "Name must be between 3 and 50 characters";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
