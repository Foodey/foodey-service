package com.foodey.server.user.model.converter;

import com.webauthn4j.data.AuthenticatorTransport;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class AuthenticatorTransportReadingConverter
    implements Converter<String, AuthenticatorTransport> {

  @Override
  public AuthenticatorTransport convert(String source) {
    return AuthenticatorTransport.create(source);
  }
}
