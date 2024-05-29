package com.foodey.server.validation.validator;

import com.foodey.server.common.regexp.PhoneNumberRegexp;
import com.foodey.server.validation.annotation.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

  private String regexp;
  private boolean optional;
  private PhoneNumberRegexp[] regions;

  @Override
  public void initialize(PhoneNumber constraintAnnotation) {
    optional = constraintAnnotation.optional();
    regexp = constraintAnnotation.regexp();
    regions = constraintAnnotation.regions();
  }

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
    if (!StringUtils.hasText(phoneNumber)) {
      return optional;
    } else if (StringUtils.hasText(regexp) && phoneNumber.matches(regexp)) {
      return true;
    } else if (regions.length > 0) {
      for (PhoneNumberRegexp regionRegexp : regions) {
        if (regionRegexp.isValid(phoneNumber)) {
          return true;
        }
      }
    }
    return com.foodey.server.utils.PhoneNumberValidator.validate(phoneNumber);
  }
}
