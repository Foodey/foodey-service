package com.foodey.server.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
@NotBlank
@Size
public @interface OptimizedName {

  @OverridesAttribute(constraint = Size.class, name = "min")
  int min() default 5;

  @OverridesAttribute(constraint = Size.class, name = "max")
  int max() default 50;
}
