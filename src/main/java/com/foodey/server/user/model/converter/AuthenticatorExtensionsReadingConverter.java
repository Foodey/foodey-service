package com.foodey.server.user.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.webauthn4j.converter.util.CborConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.extension.authenticator.RegistrationExtensionAuthenticatorOutput;
import com.webauthn4j.util.Base64UrlUtil;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class AuthenticatorExtensionsReadingConverter
    implements Converter<String, Map<String, RegistrationExtensionAuthenticatorOutput>> {

  private final CborConverter cborConverter;

  public AuthenticatorExtensionsReadingConverter(ObjectConverter objectConverter) {
    this.cborConverter = objectConverter.getCborConverter();
  }

  @Override
  public Map<String, RegistrationExtensionAuthenticatorOutput> convert(String source) {
    return cborConverter.readValue(
        Base64UrlUtil.decode(source),
        new TypeReference<Map<String, RegistrationExtensionAuthenticatorOutput>>() {});
  }
}
