package com.foodey.server.validation.annotation;

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

  String customRegexp() default "";

  Regexp[] regexp() default {
    Regexp.VIETNAM, Regexp.US, Regexp.UK, Regexp.SINGAPORE, Regexp.AUSTRALIA,
  };

  public static enum Regexp {
    VIETNAM("^(\\+84|0)(3[2-9]|5[2689]|7[06-9]|8[1-9]|9[0-46-9])\\d{7}$"),
    US("^\\+1\\d{10}$"),
    UK("^\\+44\\d{10}$"),
    SINGAPORE("^\\+65\\d{8}$"),
    AUSTRALIA("^\\+61\\d{9}$"),
    RUSSIA("^\\+7\\d{10}$"),
    GERMANY("^\\+49\\d{10}$"),
    ;

    private final String regexp;

    Regexp(String value) {
      this.regexp = value;
    }

    public String getRegexp() {
      return regexp;
    }

    /// Returns true if the phone number matches the regexps
    public boolean isValid(String phoneNumber) {
      return phoneNumber.matches(regexp);
    }
  }
}
