package com.foodey.server.validation.validator;

import com.foodey.server.validation.annotation.PhoneNumber;
import com.foodey.server.validation.annotation.PhoneNumber.Regexp;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

  String customRegexp;
  boolean optional;
  Regexp[] regexps;

  @Override
  public void initialize(PhoneNumber constraintAnnotation) {
    this.optional = constraintAnnotation.optional();
    this.customRegexp = constraintAnnotation.customRegexp();
    this.regexps = constraintAnnotation.regexp();
  }

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
    if (!optional && !StringUtils.hasText(phoneNumber)) return false;
    else if (StringUtils.hasText(customRegexp) && phoneNumber.matches(customRegexp)) return true;

    for (Regexp regexp : regexps) {
      if (regexp.isValid(phoneNumber)) return true;
    }
    return false;
  }
}
