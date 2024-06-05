package com.foodey.server.user.model.converter;

import com.webauthn4j.converter.util.CborConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.extension.authenticator.RegistrationExtensionAuthenticatorOutput;
import com.webauthn4j.util.Base64UrlUtil;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class AuthenticatorExtensionsWritingConverter
    implements Converter<Map<String, RegistrationExtensionAuthenticatorOutput>, String> {

  private final CborConverter cborConverter;

  public AuthenticatorExtensionsWritingConverter(ObjectConverter objectConverter) {
    this.cborConverter = objectConverter.getCborConverter();
  }

  @Override
  public String convert(Map<String, RegistrationExtensionAuthenticatorOutput> source) {
    return Base64UrlUtil.encodeToString(cborConverter.writeValueAsBytes(source));
  }
}
