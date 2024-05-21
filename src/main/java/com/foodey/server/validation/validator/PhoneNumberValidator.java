package com.foodey.server.validation.validator;

import com.foodey.server.validation.annotation.PhoneNumber;
import com.foodey.server.validation.annotation.PhoneNumber.CountryCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

  String regexp;
  boolean optional;
  CountryCode[] regions;

  @Override
  public void initialize(PhoneNumber constraintAnnotation) {
    this.optional = constraintAnnotation.optional();
    this.regexp = constraintAnnotation.regexp();
    this.regions = constraintAnnotation.regions();
  }

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {

    if (StringUtils.hasText(phoneNumber)
            && (StringUtils.hasText(regexp) && phoneNumber.matches(regexp))
        || Arrays.stream(regions).anyMatch(region -> region.isValid(phoneNumber))) return true;

    return optional;
  }
}
