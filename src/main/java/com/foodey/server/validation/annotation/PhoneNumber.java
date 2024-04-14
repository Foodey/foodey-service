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

  String regexp() default "";

  Region[] regions() default {
    Region.VIETNAM,
    Region.SINGAPORE,
    Region.CHINA,
    Region.INDIA,
    Region.JAPAN,
    Region.KOREA,
    Region.AUSTRALIA,
    Region.US,
    Region.CANADA,
    Region.UK,
    Region.RUSSIA,
    Region.GERMANY,
    Region.FRANCE,
  };

  public static enum Region {
    // South East Asia
    VIETNAM("^(\\+84|0|0084)(3[2-9]|5[2689]|7[06-9]|8[1-9]|9[0-46-9])\\d{7}$"),
    SINGAPORE("^(\\+65|0065)\\d{8}$"),

    // East Asia
    JAPAN("^(\\+81|0081)\\d{10}$"),
    CHINA("^(\\+86|0086)\\d{11}$"),
    KOREA("^(\\+82|0082)\\d{10}$"),

    // South Asia
    INDIA("^(\\+91|0091)\\d{10}$"),

    // Oceania
    AUSTRALIA("^(\\+61|0061)\\d{9}$"),

    // North America
    US("^(\\+1|001)\\d{10}$"),
    CANADA("^(\\+1|001)\\d{10}$"),

    // Europe
    UK("^(\\+44|0044)\\d{10}$"),
    RUSSIA("^(\\+7|007)\\d{10}$"),
    GERMANY("^(\\+49|0049)\\d{10}$"),
    FRANCE("^(\\+33|0033)\\d{9}$"),
    ;

    private final String regexp;

    Region(String value) {
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
