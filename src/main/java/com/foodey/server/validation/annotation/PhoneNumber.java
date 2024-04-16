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
    MALAYSIA("^(\\+60|0060)\\d{9}$"),
    CAMBODIA("^(\\+855|00855)\\d{8}$"),
    LAOS("^(\\+856|00856)\\d{8}$"),
    THAILAND("^(\\+66|0066)\\d{9}$"),
    INDONESIA("^(\\+62|0062)\\d{9,12}$"),
    PHILIPPINES("^(\\+63|0063)\\d{10}$"),
    MYANMAR("^(\\+95|0095)\\d{9}$"),
    BRUNEI("^(\\+673|00673)\\d{7}$"),
    TIMOR_LESTE("^(\\+670|00670)\\d{7}$"),

    // East Asia
    JAPAN("^(\\+81|0081)\\d{10}$"),
    CHINA("^(\\+86|0086)\\d{11}$"),
    KOREA("^(\\+82|0082)\\d{10}$"),
    HONG_KONG("^(\\+852|00852)\\d{8}$"),
    TAIWAN("^(\\+886|00886)\\d{9}$"),
    MACAU("^(\\+853|00853)\\d{8}$"),
    MONGOLIA("^(\\+976|00976)\\d{8}$"),
    NORTH_KOREA("^(\\+850|00850)\\d{8}$"),

    // South Asia
    INDIA("^(\\+91|0091)\\d{10}$"),
    PAKISTAN("^(\\+92|0092)\\d{10}$"),
    BANGLADESH("^(\\+880|00880)\\d{10}$"),
    SRI_LANKA("^(\\+94|0094)\\d{9}$"),
    NEPAL("^(\\+977|00977)\\d{10}$"),
    BHUTAN("^(\\+975|00975)\\d{8}$"),

    // Oceania
    AUSTRALIA("^(\\+61|0061)\\d{9}$"),
    NEW_ZEALAND("^(\\+64|0064)\\d{9}$"),
    FIJI("^(\\+679|00679)\\d{7}$"),
    SAMOA("^(\\+685|00685)\\d{7}$"),
    TONGA("^(\\+676|00676)\\d{7}$"),
    COOK_ISLAND("^(\\+682|00682)\\d{5}$"),

    // North America
    US("^(\\+1|001)\\d{10}$"),
    CANADA("^(\\+1|001)\\d{10}$"),
    MEXICO("^(\\+52|0052)\\d{10}$"),

    // South America
    BRAZIL("^(\\+55|0055)\\d{11}$"),
    ARGENTINA("^(\\+54|0054)\\d{10}$"),
    CHILE("^(\\+56|0056)\\d{9}$"),
    COLOMBIA("^(\\+57|0057)\\d{10}$"),
    PERU("^(\\+51|0051)\\d{9}$"),
    VENEZUELA("^(\\+58|0058)\\d{10}$"),
    ECUADOR("^(\\+593|00593)\\d{9}$"),
    PARAGUAY("^(\\+595|00595)\\d{9}$"),
    URUGUAY("^(\\+598|00598)\\d{9}$"),
    BOLIVIA("^(\\+591|00591)\\d{8}$"),
    GUYANA("^(\\+592|00592)\\d{7}$"),
    SURINAME("^(\\+597|00597)\\d{7}$"),
    GUYANE("^(\\+594|00594)\\d{9}$"),
    FRENCH_GUIANA("^(\\+594|00594)\\d{9}$"),

    // Europe
    UK("^(\\+44|0044)\\d{10}$"),
    RUSSIA("^(\\+7|007)\\d{10}$"),
    GERMANY("^(\\+49|0049)\\d{10}$"),
    FRANCE("^(\\+33|0033)\\d{9}$"),
    ITALY("^(\\+39|0039)\\d{10}$"),
    SPAIN("^(\\+34|0034)\\d{9}$"),
    SWEDEN("^(\\+46|0046)\\d{9}$"),
    DENMARK("^(\\+45|0045)\\d{8}$"),
    NEITHERLAND("^(\\+31|0031)\\d{9}$"),
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
